import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import MenuAppBar from './MenuBar';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <MenuAppBar/>
    <App/>
  </React.StrictMode>
);
