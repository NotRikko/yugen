import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import './index.css'
import HomePage from './pages/HomePage.tsx'
import ArtistsMain from "./pages/ArtistsMain.tsx"
import SignupPage from './features/auth/pages/SignupPage.tsx'
import LoginPage from './features/auth/pages/LoginPage.tsx'
import MainPage from './pages/MainPage.tsx'
import { UserProvider } from './UserProvider.tsx'
import { SidebarProvider} from "@/features/ui/sidebar.tsx"


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
    path: '/signup',
    element: <SignupPage />
  },
  {
    path: '/login',
    element: <LoginPage />
  },
  {
    path: '/feed',
    element: <MainPage />
  }
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
