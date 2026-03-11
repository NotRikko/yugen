import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import './index.css';
import ArtistMain from './features/artists/pages/ArtistMain.tsx'; 
import ArtistProfilePage from "./features/artists/pages/ArtistProfilePage.tsx";
import SignupPage from './features/auth/pages/SignupPage.tsx';
import LoginPage from './features/auth/pages/LoginPage.tsx';
import { UserProvider } from './features/user/UserProvider.tsx';
import AppLayout from './AppLayout.tsx';
import FeedMain from './features/feed/pages/FeedMain.tsx';
import Logout from './features/auth/components/Logout.tsx';

const router = createBrowserRouter([
  {
    path: "/",
    element: <AppLayout />,
    children: [
      { index: true, element: <FeedMain /> },
      { path: "artist", element: <ArtistMain /> },
      { path: "artist/:username", element: <ArtistProfilePage /> },
    ],
  },
  { path: "/signup", element: <SignupPage /> },
  { path: "/login", element: <LoginPage /> },
  { path: "/logout", element: <Logout /> },
]);
createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <UserProvider>
        <RouterProvider router={router} />
    </UserProvider>
  </StrictMode>
);