package br.com.tuto.agendamento.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import br.com.tuto.agendamento.job.Job;
import br.com.tuto.agendamento.job.JobRepository;

/**
 * Serviço que possui as lógicas dos jobs.
 * 
 * @author tutocury
 *
 */
@Service
public class JobService {

    private static Logger LOGGER = LoggerFactory.getLogger(JobService.class);

    @Autowired
    private AgendadorService agendadorDinamico;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Registra os Serviços dessa classe no Agendador.
     */
    private void registraJobs() {
	jobRepository.findByLigadoIsTrueAndRodandoIsFalse()
		.forEach(job -> ativaJob(job));
    }

    /**
     * Ativa o Job se ele estiver ligado porém não rodando.
     * 
     * @param job
     */
    private void ativaJob(Job job) {
	if (job.isLigado() && !job.isRodando()) {
	    agendadorDinamico.registraJob(job.getId(), () -> executarChamadaHttp(job));
	}
    }

    /**
     * Job que gerencia os Jobs que deverão ser executados.
     */
    @Scheduled(fixedRate = 20 * 1000, initialDelay = 1000)
    public void gerenciarJobs() {
	LOGGER.info("Inicia os Jobs Ligados que estão Parados");
	registraJobs();
    }

    /**
     * Retorna um Exemplo de Job.
     * 
     * @return
     */
    public String executarChamadaHttp(Job job) {
	StringBuilder url = new StringBuilder().append(job.getUrl());
	if ("GET".equals(job.getMetodo())) {
	    return executaGet(url.toString());
	} else {
	    return executaPost(url.toString(), job.getParametros());
	}
    }

    /**
     * Executa um Get na Url informada.
     * 
     * @param url
     * @return
     */
    private String executaGet(String url) {
	try {
	    return restTemplate.getForObject(url, String.class);
	} catch (HttpStatusCodeException e) {
	    return "Status Code: " + e.getRawStatusCode() + ", Error Body: " + e.getResponseBodyAsString();
	}
    }

    /**
     * Executa um Post na Url com o payload informados.
     * 
     * @param url
     * @param payload
     * @return
     */
    private String executaPost(String url, String payload) {
	try {
	    if (payload != null) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(payload, headers);
		return restTemplate.postForObject(url.toString(), request, String.class);
	    } else {
		return restTemplate.postForObject(url.toString(), null, String.class);
	    }
	} catch (HttpStatusCodeException e) {
	    return "Status Code: " + e.getRawStatusCode() + ", Error Body: " + e.getResponseBodyAsString();
	}

    }

}
