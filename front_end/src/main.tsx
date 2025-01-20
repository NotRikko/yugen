import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import './index.css'
import HomePage from './HomePage.tsx'
import ArtistsMain from "./pages/ArtistsMain.tsx"
import SignupPage from './pages/SignupPage.tsx'
import { UserProvider } from './UserProvider.tsx'

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
  }
])

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <UserProvider>
      <RouterProvider router={router} />
    </UserProvider>
  </StrictMode>,
)
