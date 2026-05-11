import RatingStars from "../common/RatingStars";
import "./ReviewCard.css";

const ReviewCard = ({ review }) => {
  return (
    <div className="review-card">
      <div className="review-header">
        <img src={review.avatar} alt={review.nickname} />
        <div>
          <strong>{review.nickname}</strong>
          <RatingStars rating={review.rating} />
          <p>{review.date}</p>
        </div>
      </div>

      <p className="review-text">{review.content}</p>

      {review.photos?.length > 0 && (
        <div className="review-photos">
          {review.photos.map((photo, index) => (
            <img key={index} src={photo} alt={`리뷰사진${index + 1}`} />
          ))}
        </div>
      )}

      <div className="review-actions">
        <button><svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-thumbs-up-icon lucide-thumbs-up"><path d="M15 5.88 14 10h5.83a2 2 0 0 1 1.92 2.56l-2.33 8A2 2 0 0 1 17.5 22H4a2 2 0 0 1-2-2v-8a2 2 0 0 1 2-2h2.76a2 2 0 0 0 1.79-1.11L12 2a3.13 3.13 0 0 1 3 3.88Z"/><path d="M7 10v12"/></svg>
        도움돼요</button>
        <button><svg xmlns="http://www.w3.org/2000/svg" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-heart-icon lucide-heart"><path d="M2 9.5a5.5 5.5 0 0 1 9.591-3.676.56.56 0 0 0 .818 0A5.49 5.49 0 0 1 22 9.5c0 2.29-1.5 4-3 5.5l-5.492 5.313a2 2 0 0 1-3 .019L5 15c-1.5-1.5-3-3.2-3-5.5"/></svg>
        좋아요</button>
      </div>
    </div>
  );
};

export default ReviewCard;