import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import './index.css'
import HomePage from './HomePage.tsx'
import ArtistsMain from "./pages/ArtistsMain.tsx"
import SignupPage from './pages/SignupPage.tsx'
import LoginPage from './pages/LoginPage.tsx'
import FeedMain from './pages/FeedMain.tsx'
import { UserProvider } from './UserProvider.tsx'
import { SidebarProvider} from "@/components/ui/sidebar"


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
    element: <FeedMain />
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
