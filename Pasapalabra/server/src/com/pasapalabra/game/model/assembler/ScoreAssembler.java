package com.pasapalabra.game.model.assembler;

import com.pasapalabra.game.model.UserScore;
import com.pasapalabra.game.model.DTO.UserScoreDTO;

/**
 * Utility class for assemble form a UserScore to a UserScoreDTO and vice versa 
 * @author ivan
 */
public class ScoreAssembler {

	
	private static ScoreAssembler instance = new ScoreAssembler();

	private ScoreAssembler(){}
	
	public static ScoreAssembler getInstance(){ return instance; }
	
	
	public UserScoreDTO assembleToDTO(UserScore nonDTO) {
		return new UserScoreDTO(nonDTO.getRightAnswered(), nonDTO.getWrongAnswered(),nonDTO.isVictory());
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
