import { useState } from "react"
import Slider from 'react-slick'
import "slick-carousel/slick/slick.css"; 
import "slick-carousel/slick/slick-theme.css";
import "../../ReactSlickStyle.css"

interface TrendingProduct {
    id: number;
    title: string;
    artist: string;
    image: string;
}


function MainTrending(): JSX.Element {
    const [trendingProducts] = useState<TrendingProduct[]>([
        {
            id: 1,
            title: "Robin",
            artist: "Hoyoverse",
            image: "https://images7.alphacoders.com/135/1354522.jpeg"
        },
        {
            id: 2,
            title: "Stelle",
            artist: "Hoyoverse",
            image: "https://upload-os-bbs.hoyolab.com/upload/2024/05/02/96651055/5736a86b9c810ffb89064756f8b0a01a_7907253663743125350.png"
        },
        {
            id: 3,
            title: "Robin",
            artist: "Hoyoverse",
            image: "https://64.media.tumblr.com/2a9dfaaf80772eadb29867173d34255d/4cade513756e5fe0-95/s1280x1920/4beea084ef85f25dcac7ca4a38ba0d6e447e5c36.png"
        }
    ])

    const settings = {
        className: "center",
        centerMode: true,
        infinite: true,
        centerPadding: "30%",
        slidesToShow: 1,
        speed: 500,
        dots: true,
        focusOnSelect: true,
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
        <div className="w-full h-svh flex flex-col items-center justify-center gap-10 overflow-x-hidden">
            <h1 className="text-4xl">Trending Right Now</h1>
            <Slider {...settings} className="w-full md:px-8 ">
                {trendingProducts.map((product, index) => (
                <div
                    key={index}
                    className="outline-none flex flex-col  h-96 md:h-[500px] lg:h-[580px] w-full bg-white rounded-lg overflow-y-visible"
                >
                    <img
                    src={product.image}
                    alt={product.title}
                    className="h-[85%] w-full object-cover rounded-md"
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