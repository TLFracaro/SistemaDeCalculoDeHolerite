package model.dao;

import model.entidade.Empregador;

public class ContextoUsuario {
    private static ContextoUsuario instancia;
    private Empregador usuarioLogado;
    
    private ContextoUsuario() {
    }
    
    public static synchronized ContextoUsuario getInstance() {
        if (instancia == null) {
            instancia = new ContextoUsuario();
        }
        return instancia;
    }
    
    public void login(Empregador usuario) {
        usuarioLogado = usuario;
    }
    
    public void logout() {
        usuarioLogado = null;
    }
    
    public Empregador getUsuarioLogado() {
        return usuarioLogado;
    }
}
