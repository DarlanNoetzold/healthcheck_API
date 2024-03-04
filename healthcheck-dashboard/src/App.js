import React from 'react';
import './App.css';
import Records from './components/Records';
import Metrics from './components/Metrics';
import ModelAccuracies from './components/ModelAccuracies';
import { Layout, Menu } from 'antd';
const { Header, Content, Footer } = Layout;

function App() {
  return (
    <Layout className="layout">
      <Header>
        <div className="logo" />
        <Menu theme="dark" mode="horizontal" defaultSelectedKeys={['2']}>
          <Menu.Item key="1">Dashboard</Menu.Item>
        </Menu>
      </Header>
      <Content style={{ padding: '0 50px' }}>
        <div className="site-layout-content">
          <Records />
          <Metrics />
          <ModelAccuracies />
        </div>
      </Content>
      <Footer style={{ textAlign: 'center' }}></Footer>
    </Layout>
  );
}

export default App;
