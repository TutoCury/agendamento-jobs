package br.com.tuto.agendamento.job;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Job {

    @Id
    private String id;

    private Long periodo;

    private boolean ligado;
    private boolean rodando;
    private LocalDateTime ultimaExecucao;

    private String url;
    private String metodo;
    private String parametros;

    public Job() {
	super();
    }

    public Job(String id, Long periodo) {
	this.id = id;
	this.periodo = periodo;
	this.ligado = true;
    }

    public Job(String id, Long periodo, String url, String metodo) {
	this(id, periodo);
	this.url = url;
	this.metodo = metodo;
    }

    public Job(String id, Long periodo, String url, String metodo, String parametros) {
	this(id, periodo, url, metodo);
	this.parametros = parametros;
    }

    public String getId() {
	return id;
    }

    public Long getPeriodo() {
	return periodo;
    }

    public void setPeriodo(Long periodo) {
	this.periodo = periodo;
    }

    public boolean isLigado() {
	return ligado;
    }

    public void ligar() {
	this.ligado = false;
    }

    public void desligar() {
	this.ligado = false;
    }

    public boolean isRodando() {
	return rodando;
    }

    public void setRodando(boolean rodando) {
	this.rodando = rodando;
    }

    public LocalDateTime getUltimaExecucao() {
	return ultimaExecucao;
    }

    public void setUltimaExecucao(LocalDateTime ultimaExecucao) {
	this.ultimaExecucao = ultimaExecucao;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getMetodo() {
	return metodo;
    }

    public void setMetodo(String metodo) {
	this.metodo = metodo;
    }

    public String getParametros() {
	return parametros;
    }

    public void setParametros(String parametros) {
	this.parametros = parametros;
    }

    public void setId(String id) {
	this.id = id;
    }

    public void setLigado(boolean ligado) {
	this.ligado = ligado;
    }

}
