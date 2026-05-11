import "./App.css";
import Button from "./components/common/Button";
// import RatingStars from "./components/common/RatingStars";
import Header from "./components/layout/Header";
import RestaurantCard from "./components/restaurant/RestaurantCard";
import SearchBar from "./components/common/SearchBar";
import ReviewCard from "./components/restaurant/ReviewCard";

function App() {

  const restaurant = {
    image: "https://images.unsplash.com/photo-1555396273-367ea4eb4db5",
    name: "천안곱창맛집",
    category: "한식",
    rating: 3,
    location: "충남 천안시",
    description: "천안에서 유명한 곱창 맛집입니다."
  };
  const review = {
    id: 1,
    nickname: "천안먹잘알",
    avatar:
      "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200",
    date: "2026.05.08",
    rating: 3,
    content:
      "곱창이 진짜 맛있고 직원분들도 친절했어요. 볶음밥까지 꼭 먹어야 됩니다.",
    photos: [
      "https://images.unsplash.com/photo-1552566626-52f8b828add9?w=500",
      "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=500",
    ],
  };
  return (
    <div>
      <Header />
      <Button>asd</Button>
      <SearchBar></SearchBar>
      <RestaurantCard restaurant={restaurant}></RestaurantCard>
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-star w-6 h-6 fill-[#FFC857] text-[#FFC857]" data-fg-0gq50="2.41:2.15996:/src/app/components/RestaurantDetailPage.tsx:175:21:7135:58:e:Star::::::hX0" data-fgid-0gq50=":r33e:"><path d="M11.525 2.295a.53.53 0 0 1 .95 0l2.31 4.679a2.123 2.123 0 0 0 1.595 1.16l5.166.756a.53.53 0 0 1 .294.904l-3.736 3.638a2.123 2.123 0 0 0-.611 1.878l.882 5.14a.53.53 0 0 1-.771.56l-4.618-2.428a2.122 2.122 0 0 0-1.973 0L6.396 21.01a.53.53 0 0 1-.77-.56l.881-5.139a2.122 2.122 0 0 0-.611-1.879L2.16 9.795a.53.53 0 0 1 .294-.906l5.165-.755a2.122 2.122 0 0 0 1.597-1.16z"></path></svg>
      <ReviewCard review={review}></ReviewCard>
    </div>
  );
}

export default App;