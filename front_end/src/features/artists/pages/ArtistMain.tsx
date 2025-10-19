import { Outlet } from "react-router-dom";

const ArtistMain: React.FC = () => {
  return (
    <div>
      <Outlet /> 
    </div>
  );
};

export default ArtistMain;