package br.com.tuto.agendamento.job;

import java.util.Arrays;
import java.util.List;

public enum JobConstant {

    EXEMPLO("exemplo"), LISTAR_JOBS("listar-jobs");

    private String id;

    private JobConstant(String id) {
	this.id = id;
    }

    public String getId() {
	return id;
    }

    public static List<JobConstant> listaTodas() {
	return Arrays.asList(values());
    }

}
