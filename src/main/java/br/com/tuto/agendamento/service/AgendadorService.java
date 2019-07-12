package br.com.tuto.agendamento.service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import br.com.tuto.agendamento.job.Execucao;
import br.com.tuto.agendamento.job.Job;
import br.com.tuto.agendamento.job.JobRepository;

/**
 * Serviço de Agendamento de Jobs.
 * 
 * @author tutocury
 *
 */
@Service
public class AgendadorService {

    private static Logger LOGGER = LoggerFactory.getLogger(AgendadorService.class);

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private JobRepository jobRepository;

    /**
     * Insere no banco todos os Jobs listados no enum JobConstant.
     */
    @PostConstruct
    public void inicializaBancoDeDados() {
	jobRepository.save(new Job("inception-dream", periodoRandom(),
		"http://localhost:8080/inception/dream", "GET"));
	jobRepository.save(new Job("who-call", periodoRandom(),
		"http://localhost:8080/inception/whoYouGonnaCall", "POST", "{ \"name\":\"GC\" }"));
	jobRepository.save(new Job("inception-error", periodoRandom(),
		"http://localhost:8080/inception/error", "GET"));
	jobRepository.save(new Job("inception-not-found", periodoRandom(),
		"http://localhost:8080/inception/searchingNemo", "GET"));
    }

    /**
     * Registra um Runnable vinculado ao id de um Job.
     * 
     * @param idJob
     * @param jobParaRodar
     */
    public void registraJob(String idJob, Execucao jobParaRodar) {
	taskScheduler.schedule(() -> {
	    jobRepository.findById(idJob).ifPresent(job -> {
		if (job.isLigado()) {
		    String retornoJob = jobParaRodar.executar();
		    LOGGER.info("Executou o Job " + idJob + ", retornou a mensagem: " + retornoJob
			    + ", proximo periodo " + job.getPeriodo());
		    job.setRodando(true);
		    job.setUltimaExecucao(LocalDateTime.now());
		    jobRepository.save(job);
		} else {
		    LOGGER.info("Job " + idJob + " está desligado");
		    job.setRodando(false);
		    jobRepository.save(job);
		}
	    });
	}, t -> agendaProximaExecucao(idJob, t.lastActualExecutionTime()));
    }

    /**
     * Agenda a próxima execução de um Job.
     * 
     * @param idJob
     * @param ultimaExecucao
     * @return
     */
    private Date agendaProximaExecucao(String idJob, Date ultimaExecucao) {
	Job job = jobRepository.findById(idJob).get();
	if (job != null && job.isLigado()) {
	    Calendar proximaExecucao = new GregorianCalendar();
	    proximaExecucao.setTime(ultimaExecucao != null ? ultimaExecucao : new Date());
	    proximaExecucao.add(Calendar.SECOND, (job.getPeriodo() > 0) ? job.getPeriodo().intValue() : 1);

	    LOGGER.info("Proximo Job vai rodar em " + proximaExecucao.getTime());
	    return proximaExecucao.getTime();
	}
	return null;
    }

    /**
     * Gera um período randômico de um a dez segundos, só pela zoeira.
     * 
     * @return
     */
    private Long periodoRandom() {
	int periodo = new Random().nextInt(10) + 1;
	return new Long(periodo);
    }

}
