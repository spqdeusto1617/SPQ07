package model.assembler;

import model.UserScore;
import model.DTO.UserScoreDTO;

public class ScoreAssembler {

	
	private static ScoreAssembler instance = new ScoreAssembler();

	private ScoreAssembler(){}
	
	public static ScoreAssembler getInstance(){ return instance; }
	
	
	public UserScoreDTO assembleToDTO(UserScore nonDTO) {
		return new UserScoreDTO(nonDTO.getRightAnswered(), nonDTO.getWrongAnswered());
	}

	/*@Override
	public User assembleFromDTO(UserDTO dto) {
	return new User(dto.getId(),
			dto.getCitizenID(), 
			dto.getNameAndSurname_s(), 
			dto.getEmail(),
			null,
			PaymentMethodAssembler.getInstance().assembleFromDTO(dto.getDefaultPaymentMethod()), 
			dto.getDob(), 
			AirportAssembler.getInstance().assembleFromDTO(dto.getDefaultAirport()));
	}*/

	
	


}
