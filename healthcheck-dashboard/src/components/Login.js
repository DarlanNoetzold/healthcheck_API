import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Form, Input, Card, notification } from 'antd';
import axios from 'axios';
import './Login.css'; // Importe seu arquivo CSS

const AUTH_API_BASE_URL = 'http://localhost:8199/healthcheck/v1/auth';

const Login = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const onFinish = async (values) => {
    setLoading(true);
    try {
      const response = await axios.post(`${AUTH_API_BASE_URL}/authenticate`, {
        email: values.email,
        password: values.password,
      });
      localStorage.setItem('authToken', response.data.access_token);
      navigate('/');
    } catch (error) {
      notification.error({
        message: 'Erro de Login',
        description: 'Email ou senha incorretos. Tente novamente.',
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container"> {/* Utilize Flexbox ou Grid para centralizar o Card */}
      <Card title="Login" className="login-card">
        <Form
          name="basic"
          initialValues={{ remember: true }}
          onFinish={onFinish}
          autoComplete="off"
        >
          <Form.Item
            label="Email"
            name="email"
            rules={[{ required: true, message: 'Por favor, insira seu e-mail!' }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            label="Senha"
            name="password"
            rules={[{ required: true, message: 'Por favor, insira sua senha!' }]}
          >
            <Input.Password />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loading} block>
              Login
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};

export default Login;
