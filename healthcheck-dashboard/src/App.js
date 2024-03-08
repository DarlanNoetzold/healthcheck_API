import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './App.css';
import Records from './components/Records';
import Metrics from './components/Metrics';
import ModelAccuracies from './components/ModelAccuracies';
import Login from './components/Login';
import { Layout, Menu, Button } from 'antd';
const { Header, Content, Footer } = Layout;

function App() {
  const handleLogout = () => {
    localStorage.removeItem('authToken');
  };

  return (
    <Router>
      <Layout className="layout">
        <Header>
          <div className="logo" />
          <Menu theme="dark" mode="horizontal" defaultSelectedKeys={['1']}>
            <Menu.Item key="1"><Link to="/">Dashboard</Link></Menu.Item>
            <Menu.Item key="2"><Link to="/metrics">Metrics</Link></Menu.Item>
            <Menu.Item key="3"><Link to="/model-accuracies">Model Accuracies</Link></Menu.Item>
            <Button onClick={handleLogout} type="primary" style={{ marginLeft: 'auto' }}><Link to="/login">Logout</Link></Button>
          </Menu>
        </Header>
        <Content style={{ padding: '0 50px' }}>
          <div className="site-layout-content">
            <Routes>
              <Route path="/" element={<Records />} />
              <Route path="/metrics" element={<Metrics />} />
              <Route path="/model-accuracies" element={<ModelAccuracies />} />
              <Route path="/login" element={<Login />} />
            </Routes>
          </div>
        </Content>
        <Footer style={{ textAlign: 'center' }}>©2023 Your App Name</Footer>
      </Layout>
    </Router>
  );
}

export default App;