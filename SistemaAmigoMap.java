package br.ufpb.dcx.sisamigo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SistemaAmigoMap {

	private List<Mensagem> mensagens;
	private Map<String,Amigo> amigos;
	private int maxMensagens;

	public SistemaAmigoMap() {
		this(1000);
	}

	public SistemaAmigoMap(int maxMensagens) {
		this.mensagens = new ArrayList<Mensagem>();
		this.amigos = new HashMap<>();
		this.maxMensagens = maxMensagens;
	}
	public void sortear2() {
		List<String> emailsDosParticipantes = new LinkedList<>();
		for (Amigo a: this.amigos.values()) {
			emailsDosParticipantes.add(a.getEmail());
		}
		boolean concluidoSorteio = false;
		while (!concluidoSorteio) {
			Collections.shuffle(emailsDosParticipantes);
			boolean deuCerto = true;
			for (int k=0; k<this.amigos.size() ; k++) {
				if (this.amigos.get(k).getEmail().equals(emailsDosParticipantes.get(k))){
					deuCerto = false;
				}
			}
			if (deuCerto) {
				concluidoSorteio = true;
			}
		}
		if (concluidoSorteio) {
			for (Amigo a : this.amigos.values()) {
				a.setAmigoSorteado(emailsDosParticipantes.remove(0));
				System.out.println("Sorteio: " +a.getEmail() +" pegou " +a.getEmailAmigoSorteado());
			}
		}
	}
	public void sortear() {
		boolean concluidoSorteio = false;
		while (!concluidoSorteio) {
			List<String> emailsJaSorteados = new LinkedList<>();
			for (int k = 0; k < this.amigos.size(); k++) {
				boolean achou = false;
				while (!achou) {
					int posicao = (int) (Math.random() * this.amigos.size());
					String emailDaPosicao = this.amigos.get(posicao).getEmail();
					if (posicao != k && !emailsJaSorteados.contains(emailDaPosicao)) {
						emailsJaSorteados.add(emailDaPosicao);
						achou = true;
						if (emailsJaSorteados.size()==this.amigos.size()) {
							concluidoSorteio = true;
						}
					}
				}

			}
			if (concluidoSorteio) {
				for (Amigo a : this.amigos.values()) {
					a.setAmigoSorteado(emailsJaSorteados.remove(0));
					System.out.println("Sorteio: " +a.getEmail() +" pegou " +a.getEmailAmigoSorteado());
				}
			}
		}
	}

	public void cadastraAmigo(String nomeAmigo, String emailAmigo) throws AmigoJaExistenteException {
		Amigo novoAmigo = new Amigo(nomeAmigo, emailAmigo);
		if (!this.amigos.containsKey(emailAmigo)) {
			this.amigos.put(emailAmigo, novoAmigo);
		} else {
			throw new AmigoJaExistenteException("Já existe essa pessoa no sistema");
		}
	}

	public Amigo pesquisaAmigo(String emailAmigo) throws AmigoInexistenteException {
		for (Amigo a : this.amigos.values()) {
			if (a.getEmail().equalsIgnoreCase(emailAmigo)) {
				return a;
			}
		}
		throw new AmigoInexistenteException("Não foi encontrado usuário com o emal " + emailAmigo);
	}

	public void enviarMensagemParaTodos(String texto, String emailRemetente, boolean ehAnonima) {
		MensagemParaTodos mensagem = new MensagemParaTodos(texto, emailRemetente, ehAnonima);
		this.mensagens.add(mensagem);
	}

	public void enviarMensagemParaAlguem(String texto, String emailRemetente, String emailDestinatario,
			boolean ehAnonima) {
		MensagemParaAlguem mensagem = new MensagemParaAlguem(texto, emailRemetente, emailDestinatario, ehAnonima);
		this.mensagens.add(mensagem);
	}

	public List<Mensagem> pesquisaMensagensAnonimas() {
		List<Mensagem> mensagensAnonimas = new ArrayList<>();
		for (Mensagem m : this.mensagens) {
			if (m.ehAnonima()) {
				mensagensAnonimas.add(m);
			}
		}
		return mensagensAnonimas;
	}

	public void configuraAmigoSecretoDe(String emailDaPessoa, String emailAmigoSorteado)
			throws AmigoInexistenteException {
		boolean achei = false;
		for (Amigo amigo : this.amigos.values()) {
			if (amigo.getEmail().equals(emailDaPessoa)) {
				achei = true;
				amigo.setAmigoSorteado(emailAmigoSorteado);
			}
		}
		if (!achei) {
			throw new AmigoInexistenteException("Não foi encontrado participante com o email" + emailDaPessoa);
		}
	}

	public List<Mensagem> pesquisaTodasAsMensagens() {
		return this.mensagens;
	}

	public String pesquisaAmigoSecretoDe(String emailDaPessoa)
			throws AmigoInexistenteException, AmigoNaoSorteadoException {
		for (Amigo amigo : this.amigos.values()) {
			if (amigo.getEmail().equals(emailDaPessoa)) {
				String emailAmigoSorteado = amigo.getEmailAmigoSorteado();
				if (emailAmigoSorteado == null) {
					throw new AmigoNaoSorteadoException("Ainda não foi sorteado o amigo secreto de " + emailDaPessoa);
				} else {
					return emailAmigoSorteado;
				}
			}
		}
		throw new AmigoInexistenteException("Não foi encontrado participante com o email" + emailDaPessoa);
	}
}
