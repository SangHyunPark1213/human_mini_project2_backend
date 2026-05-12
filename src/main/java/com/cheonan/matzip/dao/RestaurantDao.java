package com.cheonan.matzip.dao;

import com.cheonan.matzip.dto.Restaurant;
import com.cheonan.matzip.dto.RestaurantCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RestaurantDao {

    private final JdbcTemplate jdbcTemplate;

    public Long save(RestaurantCreateRequest request) {
        Long id = jdbcTemplate.queryForObject(
                "SELECT restaurant_seq.NEXTVAL FROM dual",
                Long.class
        );

        String sql = """
                INSERT INTO restaurant (
                    id, name, address, phone, category,
                    latitude, longitude, thumbnail, popular_menu
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(sql,
                id,
                request.getName(),
                request.getAddress(),
                request.getPhone(),
                request.getCategory(),
                request.getLatitude(),
                request.getLongitude(),
                request.getThumbnail(),
                request.getPopularMenu()
        );

        return id;
    }

    public List<Restaurant> findAll(String category) {

        String sql = """
            SELECT id, name, address, phone, category,
                   latitude, longitude, average_rating, review_count,
                   thumbnail, popular_menu
            FROM restaurant
            WHERE (? IS NULL OR category = ?)
            ORDER BY id DESC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Restaurant restaurant = new Restaurant();
            restaurant.setId(rs.getLong("id"));
            restaurant.setName(rs.getString("name"));
            restaurant.setAddress(rs.getString("address"));
            restaurant.setPhone(rs.getString("phone"));
            restaurant.setCategory(rs.getString("category"));
            restaurant.setLatitude(rs.getDouble("latitude"));
            restaurant.setLongitude(rs.getDouble("longitude"));
            restaurant.setAverageRating(rs.getDouble("average_rating"));
            restaurant.setReviewCount(rs.getInt("review_count"));
            restaurant.setThumbnail(rs.getString("thumbnail"));
            restaurant.setPopularMenu(rs.getString("popular_menu"));
            return restaurant;
        }, category, category);
    }

    public Optional<Restaurant> findById(Long id) {
        String sql = """
                SELECT id, name, address, phone, category,
                       latitude, longitude, average_rating, review_count,
                       thumbnail, popular_menu
                FROM restaurant
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setId(rs.getLong("id"));
                restaurant.setName(rs.getString("name"));
                restaurant.setAddress(rs.getString("address"));
                restaurant.setPhone(rs.getString("phone"));
                restaurant.setCategory(rs.getString("category"));
                restaurant.setLatitude(rs.getDouble("latitude"));
                restaurant.setLongitude(rs.getDouble("longitude"));
                restaurant.setAverageRating(rs.getDouble("average_rating"));
                restaurant.setReviewCount(rs.getInt("review_count"));
                restaurant.setThumbnail(rs.getString("thumbnail"));
                restaurant.setPopularMenu(rs.getString("popular_menu"));
                return Optional.of(restaurant);
            }
            return Optional.empty();
        }, id);
    }
    // ── 수정 ────────────────────────────────────────────
    public void update(Long id, RestaurantCreateRequest request) {
        String sql = """
            UPDATE restaurant
            SET name = ?, address = ?, phone = ?,
                category = ?, latitude = ?, longitude = ?,
                thumbnail = ?, popular_menu = ?
            WHERE id = ?
            """;
        jdbcTemplate.update(sql,
                request.getName(),
                request.getAddress(),
                request.getPhone(),
                request.getCategory(),
                request.getLatitude(),
                request.getLongitude(),
                request.getThumbnail(),
                request.getPopularMenu(),
                id
        );
    }

    // ── 삭제 ────────────────────────────────────────────
    public void delete(Long id) {
        jdbcTemplate.update(
                "DELETE FROM restaurant WHERE id = ?", id
        );
    }
}