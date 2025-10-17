import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import './index.css';
import HomePage from './pages/HomePage.tsx';
import ArtistsMain from './features/artists/pages/ArtistsMain.tsx';
import ArtistProfilePage from "@/features/artists/pages/ArtistProfilePage";
import SignupPage from './features/auth/pages/SignupPage.tsx';
import LoginPage from './features/auth/pages/LoginPage.tsx';
import MainPage from './pages/MainPage.tsx';
import { UserProvider } from './features/user/UserProvider.tsx';
import { SidebarProvider } from './shared/ui/sidebar.tsx';
import SettingsForm from './features/user/components/SettingsForm.tsx';
import FeedMain from './features/feed/pages/FeedMain.tsx';
import CartPage from './features/cart/pages/CartPage.tsx';


const router = createBrowserRouter([
  {
    
    path: '/',
    element: <HomePage />
  },
  {
    path: '/artists',
    element: <ArtistsMain />
  },
  {
    path: "/artist/:artistName",
    element: <ArtistProfilePage />,
  },
  {
    path: '/signup',
    element: <SignupPage />
  },
  {
    path: '/login',
    element: <LoginPage />
  },
  {
    path: "/",
    element: <MainPage />,
    children: [
      { path: "feed", element: <FeedMain /> },
      { path: "settings", element: <SettingsForm /> },
      { path: 'cart', element: <CartPage /> },
    ],
  },
])

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <UserProvider>
      <SidebarProvider className="!block">
        <RouterProvider router={router} />
      </SidebarProvider>
    </UserProvider>
  </StrictMode>,
)
