import React, { useEffect, useState } from "react";
import axios from "axios";
import Slider from "react-slick";

const PostImageSlider = ({ postId }) =>{
  const [images, setImages] = useState([]);

  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/files/post/${postId}`)
      .then((res) => {
        setImages(res.data);
      })
      .catch((err) => console.error(err));
  }, [postId]);

  const settings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
  };

  return (
    <div style={{ width: "400px", margin: "auto" }}>
      <h3>게시글 {postId} 이미지</h3>
      <Slider {...settings}>
        {images.map((url, idx) => (
          <div key={idx}>
            <img
              src={url}
              alt={`post-${postId}-img-${idx}`}
              style={{
                width: "100%",
                height: "auto",
                borderRadius: "10px",
              }}
            />
          </div>
        ))}
      </Slider>
    </div>
  );
}

export default PostImageSlider;