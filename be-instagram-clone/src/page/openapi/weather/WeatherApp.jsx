import { useEffect,useState } from 'react';

import '../css/index.css';

import WeatherBox from '../../../component/openapi/weather/WeatherBox';
import WeatherButton from '../../../component/openapi/weather/WeatherButton';

const  WeatherApp = () => {
  
  const cities = ["Paris", "New York", "Seoul", "Busan"]; 
  const [city, setCity] = useState("");
  const [weather, setWeather] = useState(null);

  //위치 가져오기
  const getCurrentLocation = () => {
    navigator.geolocation.getCurrentPosition((position) => {
      let lat = position.coords.latitude;
      let lon = position.coords.longitude;
      getWeatherByCurrentLocation(lat, lon);
    });
  };

  const getWeatherByCurrentLocation = async (lat, lon) => {
    let url = `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=07c8f80150954d942a79882827366bc7&units=metric`;
    let response = await fetch(url);
    let data = await response.json();
    setWeather(data);
  };

  const getWeatherByCity = async () => {
    let url = `https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=07c8f80150954d942a79882827366bc7&units=metric`;
    let response = await fetch(url);
    let data = await response.json();
    setWeather(data);
  };
  

  useEffect(() => {
    getCurrentLocation();
  }, []);

  useEffect(() => {
    console.log("[debug] >>> WeatherApp city : " , city);
    if (city == "") {
      getCurrentLocation();
    } else {
      getWeatherByCity();
    }
  }, [city]);
  
  return (
    <>
      <div className="container">
        <WeatherBox weather={weather} />
        <WeatherButton cities={cities} setCity={setCity}/>
      </div>
    </>
  );
}

export default WeatherApp;