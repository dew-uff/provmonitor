package br.uff.ic.provmonitor.business;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Businesse Helper Class.
 *
 */
public class ProvMonitorBusinessHelper {
	
	/**
	 * Generate an experiment instance ID based on the experiment Id.
	 * @param experimentId String - Experiment ID used to generate the instance Id.
	 * @return experimentInstanceId String - The generated experiment instance Id.
	 * */
	public static String generateExperimentInstanceId(String experimentId){
		Date timeStampInitExecute = Calendar.getInstance().getTime();
		SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmssS");
		String nonce = sf.format(timeStampInitExecute);
		String experimentInstanceId = experimentId + nonce;
		
		return experimentInstanceId;
	}
}
