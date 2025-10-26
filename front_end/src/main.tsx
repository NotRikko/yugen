import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import './index.css';
import HomePage from './pages/HomePage.tsx';
import ArtistMain from './features/artists/pages/ArtistMain.tsx'; 
import ArtistsMain from './features/artists/pages/ArtistsMain.tsx';
import ArtistProfilePage from "@/features/artists/pages/ArtistProfilePage";
import SignupPage from './features/auth/pages/SignupPage.tsx';
import LoginPage from './features/auth/pages/LoginPage.tsx';
import MainPage from './pages/MainPage.tsx';
import FollowMain from './features/follow/pages/FollowMain.tsx';
import { UserProvider } from './features/user/UserProvider.tsx';
import { SidebarProvider } from './shared/ui/sidebar.tsx';
import SettingsForm from './features/user/components/SettingsForm.tsx';
import FeedMain from './features/feed/pages/FeedMain.tsx';
import CartPage from './features/cart/pages/CartPage.tsx';
import ArtistDashBoard from './features/artists/pages/ArtistDashBoard.tsx';
import { CartProvider } from './features/cart/CartProvider.tsx';
import ProductPage from './features/products/pages/ProductPage.tsx';
import { ProductProvider } from './features/products/ProductProvider.tsx';

const router = createBrowserRouter([
  {
    path: '/',
    element: <HomePage />, 
  },
  {
    path: '/',
    element: <MainPage />, 
    children: [
      { path: 'feed', element: <FeedMain /> },
      { path: 'settings', element: <SettingsForm /> },
      { path: 'cart', element: <CartPage /> },
      {path: 'products', element: <ProductPage />},
      { path: 'following', element: <FollowMain />},
      {
        path: 'artist',
        element: <ArtistMain />,
        children: [
          { path: 'artists', element: <ArtistsMain /> },
          { path: 'dashboard', element: <ArtistDashBoard /> },
          { path: ':artistName', element: <ArtistProfilePage /> },
        ],
      },
    ],
  },
  {
    path: '/signup',
    element: <SignupPage />,
  },
  {
    path: '/login',
    element: <LoginPage />,
  },
]);

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <UserProvider>
      <CartProvider>
        <ProductProvider>
          <SidebarProvider className="!block">
            <RouterProvider router={router} />
          </SidebarProvider>
        </ProductProvider>
      </CartProvider>
    </UserProvider>
  </StrictMode>
);