import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import './index.css'
import HomePage from './HomePage.tsx'
import ProductsMain from "./pages/ProductsMain"
import CollectionPage from './components/CollectionPage.tsx'


const router = createBrowserRouter([
  {
    path: '/',
    element: <HomePage />
  },
  {
    path: '/collections',
    element: <ProductsMain />,
  },
  {
    path: '/collections/:collectionName',
    element: <CollectionPage />,
  },
])

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
)
