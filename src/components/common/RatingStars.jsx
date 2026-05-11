import "./RatingStars.css";

const RatingStars = ({ rating = 0, size = "md" }) => {
  return (
    <div className={`rating-stars rating-${size}`}>
      {[1, 2, 3, 4, 5].map((star) => (
        <span key={star} className={star <= Math.round(rating) ? "filled" : ""}>
          ★
        </span>
      ))}
      <strong>{rating.toFixed(1)}</strong>
    </div>
  );
};

export default RatingStars;