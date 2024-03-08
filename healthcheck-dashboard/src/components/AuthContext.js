import React, { createContext, useState } from 'react';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [authToken, setAuthToken] = useState(localStorage.getItem('authToken'));

    const logout = () => {
        setAuthToken(null);
        localStorage.removeItem('authToken');
        // Aqui você pode redirecionar para a tela de login ou fazer qualquer outra limpeza necessária
    };

    return (
        <AuthContext.Provider value={{ authToken, setAuthToken, logout }}>
            {children}
        </AuthContext.Provider>
    );
};