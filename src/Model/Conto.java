package Model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Conto {
	private String titolare;
	protected String aperturaConto;
	protected double saldo;
	protected List<String[]> info;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	public Conto() {
		info = new ArrayList<>();
	}
	
	
	
	public Conto(String aperturaConto, double saldo) {
		
	}
	
	
	public Conto(String titolare, String formattedDate, double saldo) {
		info = new ArrayList<>();
		addInitialRow(formattedDate);
		this.titolare = titolare;
		this.aperturaConto = formattedDate;
		this.saldo = saldo;

	}
	
	public void addInitialRow(String dataApertura) {
		String[] firstRow= new String[]{dataApertura, "1000.0", "Apertura", "1000","0"};
        info.add(firstRow);
    }
	
	public String getTitolare() {
		return titolare;
	}
	public void setTitolare(String titolare) {
		this.titolare = titolare;
	}
	public String getAperturaConto() {
		return aperturaConto;
	}
	public void setAperturaConto(String aperturaConto) {
		this.aperturaConto = aperturaConto;
	}
	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	private String getDateR() {
		LocalDate minDate;
		Calendar calendar = Calendar.getInstance();
	    int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Note: Calendar months are 0-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
		String dataPrece = info.stream()
	              .reduce((primo, secondo) -> secondo) // Riduci gli array in modo che rimanga solo l'ultimo
	              .map(array -> (array.length > 0) ? array[0] : "")
	              .orElse("");
		  
		try {
		    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		    minDate = LocalDate.parse(dataPrece, dateFormatter);
		    long minTime = minDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
	        long maxTime = minDate.plusMonths(3).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
	        long randomTime = minTime + (long) (Math.random() * (maxTime - minTime));

	        calendar.setTimeInMillis(randomTime);
		    
		} catch (DateTimeParseException e) {
		    e.printStackTrace();
		}
		
		return dateFormat.format(calendar.getTime());

    }
	
	public void preleva(Scanner in) {
		double prelievo=0;
		System.out.println("Inserire quota da prelevare: ");
		prelievo=in.nextDouble();
		this.saldo=this.saldo-prelievo;
		
		info.add(
	         Stream.of(
	             String.valueOf(getDateR()),
	             String.valueOf(this.saldo),
	             "Preleva",
	             String.valueOf(prelievo),
	             "0"
	         ).toArray(String[]::new)
	     );
	}
	
	
	
	public void preleva(Scanner in,double limite) {
    	double prelievo=0;
    	do {
    	System.out.println("Inserire quota da prelevare: ");
		prelievo =in.nextDouble();
		if(prelievo>limite)
			System.out.println("Inserire importo < 1000");
		}while(prelievo>1000);
		this.saldo=this.saldo-prelievo;
		
		info.add(
		         Stream.of(
		             String.valueOf(getDateR()),
		             String.valueOf(this.saldo),
		             "Preleva",
		             String.valueOf(prelievo),
		             "0"
		         ).toArray(String[]::new)
		     );
		
	}
	
	public void versa(Scanner in) {
		double versamento=0;
		System.out.println("Inserire quota da versare: ");
		versamento=in.nextDouble();
		this.saldo=this.saldo+versamento;
		
		info.add(
		         Stream.of(
		             String.valueOf(getDateR()),
		             String.valueOf(this.saldo),
		             "Versa",
		             String.valueOf(versamento),
		             "0"
		         ).toArray(String[]::new)
		     );
	}
	
	
	public void pulisciOperazioniVecchie(List<String[]> info, String tipo) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	    try {
	        String[] ultimaRiga = info.get(info.size() - 1);
	        String[] penultimaRiga = info.get(info.size() - 2);
	        String dataUltOpe = ultimaRiga[0];
	        String dataPenultOpe = penultimaRiga[0];

	        Date dateDataUltOpe = sdf.parse(dataUltOpe);
	        Date dateDataPenultOpe = sdf.parse(dataPenultOpe);

	        // Verifica se l'anno di dateDataPenultOpe non è successivo a quello di dateDataUltOpe
	        Calendar calUltima = Calendar.getInstance();
	        Calendar calPenultima = Calendar.getInstance();

	        calUltima.setTime(dateDataUltOpe);
	        calPenultima.setTime(dateDataPenultOpe);

	        int annoUltima = calUltima.get(Calendar.YEAR);
	        int annoPenultima = calPenultima.get(Calendar.YEAR);
	        
	        if (annoPenultima < annoUltima) {
	        	String[] informazioniUltimaOperazione = info.get(info.size() - 1);
	        	generaPdf(tipo);
                info.clear();
                info.add(informazioniUltimaOperazione);
	        }
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	}
	
	public void generaPdf(String tipo){
		
		Document document = new Document();
		String contRiga;
		
		String[] penultimaRiga = info.get(info.size() - 2); // Prendi la penultima riga
		String dataOpePrec = penultimaRiga[0];
		
		LocalDate currentDate = LocalDate.now();
		int year = currentDate.getYear();
		int month = currentDate.getMonthValue();
		int day = currentDate.getDayOfMonth();
		
		System.out.println("C:/Users/Betacom/Desktop/EC_"+titolare+"_"+dataOpePrec+".pdf");
		try {
			PdfWriter.getInstance(document, new FileOutputStream("C:/Users/Betacom/Desktop/EC"+ tipo +"_"+titolare+"_"+dataOpePrec.substring(6)+".pdf"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		document.open();
		Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
		PdfPTable table = new PdfPTable(info.get(0).length);
		List<Paragraph> paragraphs = new ArrayList<>();
		
		Font textColorFont = new Font();
        textColorFont.setColor(BaseColor.BLUE);

		
		
		
		paragraphs.add(new Paragraph("-------------------------------------------------------------------"));
		paragraphs.add(new Paragraph("Estratto Conto "+ tipo +" | Correntista: "+titolare.toUpperCase()+" | Data: "+ dataOpePrec.substring(6)));		
		paragraphs.add(new Paragraph("-------------------------------------------------------------------"));
		paragraphs.add(new Paragraph(" "));

		String[] intestazione = {"DATA", "SALDO", "OPERAZIONE", "IMPORTO", "INTERESSE"};
		for (String colonna : intestazione) {
		    PdfPCell cell = new PdfPCell(new Phrase(colonna, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK)));
		    table.addCell(cell);
		}

		for (int rowIndex = 0; rowIndex < info.size() - 1; rowIndex++) {
		    String[] row = info.get(rowIndex);

		    for (int i = 0; i < row.length; i++) {
		        PdfPCell cell;
		        if (i == row.length - 1) {
		            if ("Preleva".equals(row[0])) {
		                cell = new PdfPCell(new Phrase(row[i], new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.RED)));
		            } else if ("Versa".equals(row[0])) {
		                cell = new PdfPCell(new Phrase(row[i], new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.GREEN)));
		            } else {
		                cell = new PdfPCell(new Phrase(row[i]));
		            }
		        } else {
		            cell = new PdfPCell(new Phrase(row[i]));
		        }
		        table.addCell(cell);
		    }
		}
		
		
            try {
            	for (Paragraph paragraph : paragraphs) {
                    document.add(paragraph);
                }
				document.add(table);
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double netto= Double.parseDouble(penultimaRiga[4])-(Double.parseDouble(penultimaRiga[4]) * 0.26);
        double saldoFinale = Double.parseDouble(penultimaRiga[1]) + netto;
        paragraphs.clear();    
		paragraphs.add(new Paragraph("-------------------------------------------------------------------"));
		paragraphs.add(new Paragraph("Interessi LORDI finali: " + decimalFormat.format(Double.parseDouble(penultimaRiga[4]))));
		paragraphs.add(new Paragraph("Interessi NETTI finali: " + decimalFormat.format(netto)));
		paragraphs.add(new Paragraph("Saldo finale (+ interessi): " + decimalFormat.format(saldoFinale)));
		paragraphs.add(new Paragraph("-------------------------------------------------------------------"));

       
		
		
		try {
			for (Paragraph paragraph : paragraphs) {
                document.add(paragraph);
            }
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		document.close();
	}
	

	public void setInfo(List<String[]> info) {
		this.info = info;
	}

	public List<String[]> getInfo() {
	    return info;
	}
	
	@Override
	public String toString() {
		return "Conto [titolare=" + this.titolare + ", aperturaConto=" + aperturaConto + ", saldo=" + saldo + "]";
	}

	public double primoVersamento() {
		return this.saldo = this.saldo+1000;
		
		
	}
	
	
}