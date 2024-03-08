import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Form, Input, notification } from 'antd';
import axios from 'axios';

const AUTH_API_BASE_URL = 'http://177.22.91.106:8199/healthcheck/v1/auth';

const Login = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const onFinish = async (values) => {
    setLoading(true);
    try {
      // Chamada de API para login
      const response = await axios.post(`${AUTH_API_BASE_URL}/authenticate`, {
        email: values.email,
        password: values.password,
      });
      // Armazenar o token retornado
      localStorage.setItem('authToken', response.data.access_token);
      console.log('Login Success:', values);
      // Se login bem-sucedido, navegar para a página inicial
      navigate('/');
    } catch (error) {
      console.error('Login failed:', error);
      notification.error({
        message: 'Erro de Login',
        description: 'Email ou senha incorretos. Tente novamente.',
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <Form
      name="basic"
      labelCol={{ span: 8 }}
      wrapperCol={{ span: 16 }}
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

      <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
        <Button type="primary" htmlType="submit" loading={loading}>
          Login
        </Button>
      </Form.Item>
    </Form>
  );
};

export default Login;
