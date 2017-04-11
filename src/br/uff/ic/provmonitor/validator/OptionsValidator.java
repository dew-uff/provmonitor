package br.uff.ic.provmonitor.validator;

import org.apache.commons.cli.CommandLine;

import br.uff.ic.provmonitor.enums.MethodType;
import br.uff.ic.provmonitor.exceptions.ValidateException;

public class OptionsValidator {

	public static void validate(CommandLine cmd) throws ValidateException{
		if(!cmd.hasOption("m")) {
		    // If the m param is not informed throw exception
			throw new ValidateException("The method must be informed: -m parameter");
		}
		try{
			Integer methodId = Integer.parseInt(cmd.getOptionValue("m"));
		
			switch (MethodType.valueOf(methodId)){
			case INITIALIZE_EXPERIMENT_EXECUTION:
				if(!cmd.hasOption("ei")){
					throw new ValidateException("The experimentId must be informed: -ei parameter");
				}
				if(!cmd.hasOption("wp")){
					throw new ValidateException("The workspace path must be informed: -wp parameter");
				}
				if(!cmd.hasOption("cR")){
					throw new ValidateException("The central repository path must be informed: -cR parameter");
				}
				break;
			case FINALIZE_EXPERIMENT_EXECUTION:
				{
					if(!cmd.hasOption("eii")){
						throw new ValidateException("The experimentInstanceId must be informed: -eii parameter");
					}
					if(!cmd.hasOption("edDt")){
						throw new ValidateException("The end time must be informed: -edDt parameter");
					}
					if(!cmd.hasOption("cR")){
						throw new ValidateException("The end central repository must be informed: -cR parameter");
					}
				}
				break;
			case NOTIFY_ACTIVITY_EXECUTION_ENDING:
				{
					if(!cmd.hasOption("aii")){
						throw new ValidateException("The activityInstanceId must be informed: -aii parameter");
					}
					if(!cmd.hasOption("context")){
						throw new ValidateException("The context must be informed: -context parameter");
					}
				}
				break;
			case NOTIFY_ACTIVITY_EXECUTION_STARTUP:
				{
					if(!cmd.hasOption("aii")){
						throw new ValidateException("The activityInstanceId must be informed: -aii parameter");
					}
					if(!cmd.hasOption("context")){
						throw new ValidateException("The context must be informed: -context parameter");
					}
					if(!cmd.hasOption("wp")){
						throw new ValidateException("The workspace path must be informed: -wp parameter");
					}
				}
				break;
			case NOTIFY_DECISION_POINT_ENDING:
				{
					if(!cmd.hasOption("di")){
						throw new ValidateException("The decisionPointId must be informed: -di parameter");
					}
					if(!cmd.hasOption("ov")){
						throw new ValidateException("The optionValue must be informed: -ov parameter");
					}
					if(!cmd.hasOption("context")){
						throw new ValidateException("The context must be informed: -context parameter");
					}
				}
				break;
			case NOTIFY_PROCESS_EXECUTION_ENDING:
				{
					if(!cmd.hasOption("pii")){
						throw new ValidateException("The processInstanceId must be informed: -pii parameter");
					}
					if(!cmd.hasOption("context")){
						throw new ValidateException("The context must be informed: -context parameter");
					}
					if(!cmd.hasOption("stDt")){
						throw new ValidateException("The start time must be informed: -stDt parameter");
					}
					if(!cmd.hasOption("edDt")){
						throw new ValidateException("The end time must be informed: -edDt parameter");
					}
				}
				break;
			case NOTIFY_PROCESS_EXECUTION_STARTUP:
				{
					if(!cmd.hasOption("pii")){
						throw new ValidateException("The processInstanceId must be informed: -pii parameter");
					}
					if(!cmd.hasOption("context")){
						throw new ValidateException("The context must be informed: -context parameter");
					}
					if(!cmd.hasOption("stDt")){
						throw new ValidateException("The star time must be informed: -stDt parameter");
					}
				}
				break;
			case PUBLISH_ARTIFACT_VALUE_LOCATION:
				{
					if(!cmd.hasOption("ai")){
						throw new ValidateException("The artifactId must be informed: -ai parameter");
					}
					if(!cmd.hasOption("context")){
						throw new ValidateException("The context must be informed: -context parameter");
					}
					if(!cmd.hasOption("hUrl")){
						throw new ValidateException("The hostURL must be informed: -hUrl parameter");
					}
					if(!cmd.hasOption("hPath")){
						throw new ValidateException("The hostLocalPath must be informed: -hPath parameter");
					}
				}
				break;
			case SET_ARTIFACT_VALUE:
				{
					if(!cmd.hasOption("ai")){
						throw new ValidateException("The artifactId must be informed: -ai parameter");
					}
					if(!cmd.hasOption("av")){
						throw new ValidateException("The artifactValue must be informed: -av parameter");
					}
					if(!cmd.hasOption("context")){
						throw new ValidateException("The context must be informed: -context parameter");
					}
				}
				break;
			}
		
		}catch(NumberFormatException e){
			//Parse exception
			throw new ValidateException("Invalid method type: review param -m value");
		}
	}

}
