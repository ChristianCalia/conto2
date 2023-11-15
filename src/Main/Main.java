package Main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import Model.Conto;
import Model.ContoCorrente;
import Model.ContoDeposito;
import Model.ContoInvestimento;

public class Main {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String formattedDate = dataRandom();
		int scelta = 0,scelta2 = 0;
		Conto conto = creaConto(formattedDate, in);
		Conto contoCorrente = creaContoCorrente(conto);
		Conto contoDeposito = creaContoDeposito(conto);
		Conto contoInvestimento = creaContoInvestimento(conto);
		gestioneProg(in,scelta,scelta2,conto,contoCorrente,contoDeposito,contoInvestimento);
		
		in.close();
	}
	
	public static void gestioneProg(Scanner in,int scelta,int scelta2,Conto conto,Conto contoCorrente,Conto contoDeposito,Conto contoInvestimento) {
		do {
			menuGen(conto.getTitolare());
			scelta=in.nextInt();
			switch(scelta) {
			case 1:
				System.out.println("\nCONTO CORRENTE\n");
				do {
				menuCont("corrente");
				scelta2= in.nextInt();
					switch(scelta2) {
					case 1:
						System.out.println(contoCorrente.toString());
						break;
					case 2:
						contoCorrente.versa(in);
						((ContoCorrente) contoCorrente).generaInteressi();
						break;
					case 3:
						contoCorrente.preleva(in);
						((ContoCorrente) contoCorrente).generaInteressi();
						break;
					case 0:
						System.out.println("Ritorno al menu precedente\n\n");
						break;
					default:
						System.out.println("errato inserimento");
					}
				}while(scelta2!=0);	
				
				break;
			case 2:
				System.out.println("\nCONTO DEPOSITO\n");
				do {
					menuCont("deposito");
					scelta2= in.nextInt();
						switch(scelta2) {
						case 1:
							System.out.println(contoDeposito.toString());
							break;
						case 2:
							contoDeposito.versa(in);
							((ContoDeposito) contoDeposito).generaInteressi();
							break;
						case 3:
							contoDeposito.preleva(in,1000);
							((ContoDeposito) contoDeposito).generaInteressi();
							break;
						case 0:
							System.out.println("Ritorno al menu precedente\n\n");
							break;
						default:
							System.out.println("errato inserimento");
						}
					}while(scelta2!=0);	
				break;
			case 3:
				System.out.println("\nCONTO INVESTIMENTO\n");
				do {
					menuCont("investimento");
					scelta2= in.nextInt();
						switch(scelta2) {
						case 1:
							System.out.println(contoInvestimento.toString());
							break;
						case 2:
							contoInvestimento.versa(in);
							((ContoInvestimento) contoInvestimento).generaInteressi();
							break;
						case 3:
							contoInvestimento.preleva(in);
							((ContoInvestimento) contoInvestimento).generaInteressi();
							break;
						case 0:
							System.out.println("Ritorno al menu precedente\n\n");
							break;
						default:
							System.out.println("errato inserimento");
						}
					}while(scelta2!=0);	
				break;
			case 4:
				System.out.println("Conto Corrente:");
			    stampaInformazioni(contoCorrente);

			    System.out.println("Conto Deposito:");
			    stampaInformazioni(contoDeposito);

			    System.out.println("Conto Investimento:");
			    stampaInformazioni(contoInvestimento);
				break;
			case 0:
				System.out.println("Grazie e arrivederci");
				break;
			default:
				System.out.println("errato inserimento");
			}
			
		}while(scelta!=0);
	}
	
	public static void stampaInformazioni(Conto conto) {
	    List<String[]> info = conto.getInfo();
	    for (String[] row : info) {
	        System.out.print("Data: " + row[0] + ", ");
	        System.out.print("Saldo: " + row[1] + ", ");
	        System.out.print("Tipo operazione: " + row[2] + ", ");
	        System.out.print("Importo: " + row[3] + ", ");
	        System.out.println("Interessi maturati: " + row[4]);
	    }
	    System.out.println("-----------------------------------");
	}
	
	public static void menuCont(String tipoConto) {
		System.out.println("1)Visualizza conto "+tipoConto+": ");
		System.out.println("2)Versa");
		System.out.println("3)Preleva");
		System.out.println("0)Torna al menù precedente");
		System.out.println("inserire scelta: ");
	}
	public static void menuGen(String tit) {
		System.out.println("Conto di: " +tit);
		System.out.println("1) Conto corrente: ");
		System.out.println("2) Conto deposito: ");
		System.out.println("3) Conto di investimento: ");
		System.out.println("4) Stampa conti");
		System.out.println("0) chiudi");
		System.out.println("Inserire scelta: ");
	}
	
	
	public static Conto creaConto(String data,Scanner in) {

		Conto conto = new Conto();
		
		System.out.println("Inserire intestatario conto: ");
		conto.setTitolare(in.nextLine());
		conto.setAperturaConto(data);
		conto.setSaldo(conto.primoVersamento());
		
		conto.addInitialRow(data);
		
		return conto;
	}
	
	public static Conto creaContoInvestimento(Conto conto) {
		ContoInvestimento contoInvestimento = new ContoInvestimento();
		
		contoInvestimento.setTitolare(conto.getTitolare());
		contoInvestimento.setAperturaConto(conto.getAperturaConto());
		contoInvestimento.setSaldo(conto.getSaldo());
		contoInvestimento.addInitialRow(conto.getAperturaConto());
		return contoInvestimento;
	}
	
	public static Conto creaContoCorrente(Conto conto) {
		ContoCorrente contoCorrente = new ContoCorrente();
		
		contoCorrente.setTitolare(conto.getTitolare());
		contoCorrente.setAperturaConto(conto.getAperturaConto());
		contoCorrente.setSaldo(conto.getSaldo());
		contoCorrente.addInitialRow(conto.getAperturaConto());
		
		return contoCorrente;
	}
	
	public static Conto creaContoDeposito(Conto conto) {
		ContoDeposito contoDeposito = new ContoDeposito();
		
		contoDeposito.setTitolare(conto.getTitolare());
		contoDeposito.setAperturaConto(conto.getAperturaConto());
		contoDeposito.setSaldo(conto.getSaldo());
		contoDeposito.addInitialRow(conto.getAperturaConto());
		return contoDeposito;
	}
	
	public static String dataRandom() {
		
		Random random = new Random();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2021);
        int dayOfYear = random.nextInt(calendar.getActualMaximum(Calendar.DAY_OF_YEAR)) + 1;
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
        Date randomDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(randomDate);
        
        return formattedDate;
	}
	

}