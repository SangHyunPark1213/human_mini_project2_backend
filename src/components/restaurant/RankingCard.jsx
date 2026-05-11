import RatingStars from "../common/RatingStars";
import "./RankingCard.css";

const RankingCard = ({ rank, restaurant }) => {
  return (
    <div className="ranking-card">
      <div className="rank-badge">#{rank}</div>
      <img src={restaurant.image} alt={restaurant.name} />

      <div className="ranking-info">
        <h3>{restaurant.name}</h3>
        <p>{restaurant.location}</p>
        <RatingStars rating={restaurant.rating} size="sm" />
        <span>리뷰 {restaurant.reviewCount}개</span>
      </div>
    </div>
  );
};

export default RankingCard;