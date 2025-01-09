import { useState } from "react"
import Slider from 'react-slick'
import "slick-carousel/slick/slick.css"; 
import "slick-carousel/slick/slick-theme.css";
import "./ReactSlickStyle.css"



function MainTrending() {
    const [trendingProducts, setTrendingProducts] = useState([
        {
            title: "Robin",
            artist: "Hoyoverse",
            image: "https://images7.alphacoders.com/135/1354522.jpeg"
        },
        {
            title: "Stelle",
            artist: "Hoyoverse",
            image: "https://upload-os-bbs.hoyolab.com/upload/2024/05/02/96651055/5736a86b9c810ffb89064756f8b0a01a_7907253663743125350.png"
        },
        {
            title: "Robin",
            artist: "Hoyoverse",
            image: "https://64.media.tumblr.com/2a9dfaaf80772eadb29867173d34255d/4cade513756e5fe0-95/s1280x1920/4beea084ef85f25dcac7ca4a38ba0d6e447e5c36.png"
        }
    ])

    const settings = {
        className: "center",
        centerMode: true,
        infinite: true,
        centerPadding: "25%",
        slidesToShow: 1,
        speed: 500,
        dots: true,
        responsive:[
            {
                breakpoint: 1000,
                settings: {
                    slidesToShow: 1,
                    centerPadding: "10%"
                }
            },
            {
                breakpoint: 480,
                settings: {
                    centerPadding: "0%",
                    arrows: false
                }
            }
        ]
    }
    return(
        <div className="w-full h-svh flex flex-col items-center justify-center gap-10 overflow-hidden ">
            <h1 className="text-4xl">Trending Products</h1>
            <Slider {...settings} className="w-full md:px-8">
                {trendingProducts.map((product, index) => (
                <div
                    key={index}
                    className="p-8 outline-none flex flex-col  h-96 md:h-[400px] lg:h-[580px] w-full bg-white rounded-lg "
                >
                    <img
                    src={product.image}
                    alt={product.title}
                    className="h-full w-full object-cover rounded-md"
                    />
                    <h3 className="text-lg font-semibold mt-4">{product.title}</h3>
                    <h3 className="text-sm text-gray-500">{product.artist}</h3>
                </div>
                ))}
            </Slider>
        </div>
    )
}

export default MainTrending