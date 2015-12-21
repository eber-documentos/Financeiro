package br.com.eber.financeiro.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.eber.financeiro.model.Lancamento;
import br.com.eber.financeiro.model.Pessoa;
import br.com.eber.financeiro.model.TipoLancamento;
import br.com.eber.financeiro.repository.Lancamentos;
import br.com.eber.financeiro.repository.Pessoas;
import br.com.eber.financeiro.service.CadastroLancamentos;
import br.com.eber.financeiro.service.NegocioException;

@Named
@ViewScoped
public class CadastroLancamentoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CadastroLancamentos cadastro;

	@Inject
	private Pessoas pessoas;
	
	@Inject
	private Lancamentos lancamentos;

	private Lancamento lancamento = new Lancamento();
	private List<Pessoa> todasPessoas;

	public void prepararCadastro() {
		todasPessoas = pessoas.todas();
		
		if (lancamento == null) {
			lancamento = new Lancamento();
		}
	}

	public void salvar() {
		FacesContext context = FacesContext.getCurrentInstance();

		try {
			cadastro.salvar(lancamento);
			lancamento = new Lancamento();
			context.addMessage(null, new FacesMessage("Lançamento salvo com sucesso!"));
		} catch (NegocioException e) {
			FacesMessage mensagem = new FacesMessage(e.getMessage());
			mensagem.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, mensagem);
		}
	}

	public void dataVencimentoAlterada(AjaxBehaviorEvent event) {
		if (lancamento.getDataPagamento() == null) {
			lancamento.setDataPagamento(lancamento.getDataVencimento());
		}
	}
	
	public List<String> pesquisarDescricoes(String descricao) {
		return lancamentos.descricoesQueContem(descricao);
	}

	public List<Pessoa> getTodasPessoas() {
		return todasPessoas;
	}

	public TipoLancamento[] getTiposLancamentos() {
		return TipoLancamento.values();
	}

	public Lancamento getLancamento() {
		return lancamento;
	}
	
	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}
}
