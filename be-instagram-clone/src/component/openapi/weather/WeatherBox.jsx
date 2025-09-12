import React from "react";

const WeatherBox = ({weather}) => {
  /*
  현재위치, 섭씨, 화씨, 날씨
  */
  return (
    <div className="weather-box">
      <div> {weather?.name} </div>
      <div> {weather?.main.temp} C , {weather?.main.temp * 1.8 +32} F </div>
      <div> {weather?.weather[0].description} </div>
    </div>
  );
};

export default WeatherBox;