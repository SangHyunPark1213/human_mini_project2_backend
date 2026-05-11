import RatingStars from "../common/RatingStars";
import "./RestaurantCard.css";

const RestaurantCard = ({ restaurant }) => {
  return (
    <div className="restaurant-card">
      <div className="restaurant-img-wrap">
        <img src={restaurant.image} alt={restaurant.name} />
        <button className="heart-btn">♡</button>
      </div>

      <div className="restaurant-content">
        <span className="category-tag">{restaurant.category}</span>
        <h3>{restaurant.name}</h3>

        <RatingStars rating={restaurant.rating} />

        <p className="location">📍 {restaurant.location}</p>
        <p className="desc">{restaurant.description}</p>

        <button className="detail-btn">자세히 보기</button>
      </div>
    </div>
  );
};

export default RestaurantCard;