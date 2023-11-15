package Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ContoCorrente extends Conto{
	private double tassoInteresse = 0.07;
	private static final Logger logger = LogManager.getLogger(ContoCorrente.class);
	private double interest = 0;
	
	public ContoCorrente() {
		super();
	}
	
	public ContoCorrente(String titolare, String aperturaConto, double saldo, double tassoInteresse) {
        super(titolare, aperturaConto, saldo);
        this.tassoInteresse = tassoInteresse;
    }

	
	@Override
    public void addInitialRow(String dataApertura) {
        String[] firstRow = new String[]{dataApertura, "1000.0", "Apertura", "1000", "0"};
        info.add(firstRow);
    }
	
    public void generaInteressi() {
    	
    	pulisciOperazioniVecchie(info,"Corrente");
    	
    	
    	if(info.size()==1) {
    		System.out.println("lista con 1 solo elemento");
    	}else {
	    	String[] ultimaRiga = info.get(info.size() - 1); // Prendi l'ultima riga
	    	long differenzaInGiorni = 0;
	    	
	    	String[] penultimaRiga = info.get(info.size() - 2); // Prendi la penultima riga
	    	String dataUltOpe = ultimaRiga[0]; // Prendi il primo valore dell'ultima riga
	    	String dataOpePrec = penultimaRiga[0];
	    	String ultimoInteresse = penultimaRiga[penultimaRiga.length - 1]; // Prendi l'ultimo valore interesse
	    	double doublePenltimoInteresse = Double.parseDouble(ultimoInteresse); // Esegui il cast a double
	    	String ultimoSaldo = penultimaRiga[penultimaRiga.length - 2]; // Prendi l'ultimo valore saldo
	    	double doublePenltimoSaldo = Double.parseDouble(ultimoSaldo);
	    	
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // Modifica il formato in base ai tuoi dati
	    	try {
				Date dateDataUltOpe = sdf.parse(dataUltOpe);
				Date dateDataOpePrec = sdf.parse(dataOpePrec);
				System.out.println("Data ultima op: " + dateDataUltOpe);
				System.out.println("Data prec op: " + dateDataOpePrec);
				long differenzaInMillisecondi = dateDataUltOpe.getTime() - dateDataOpePrec.getTime();
		    	differenzaInGiorni = TimeUnit.DAYS.convert(differenzaInMillisecondi, TimeUnit.MILLISECONDS);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	 
	    	System.out.println("Giorni di differenza: " + differenzaInGiorni);
	    	
	    	double tassIntGiorn = tassoInteresse / 365;
	    	double saldoInteres = tassIntGiorn * doublePenltimoSaldo;
	    	interest = differenzaInGiorni * saldoInteres;
	
	    	logger.debug("Tasso Interesse: {}", tassoInteresse);
	    	logger.debug("Tasso Interesse Giornaliero: {}", tassIntGiorn);
	    	logger.debug("Saldo Interesse: {}", saldoInteres);
	    	logger.debug("Interesse: {}", interest);
	    	
	    	
	        ultimaRiga[4] = String.valueOf((Double.parseDouble(ultimoInteresse)) + interest); // Update the interest
	
	        // Now, add this updated row back to the 'info' list
	        info.set(info.size() - 1, ultimaRiga);
    	}
    }

    

	@Override
    public String toString() {
        
		return super.toString() + "Tasso di interesse: " + tassoInteresse;
    }
}