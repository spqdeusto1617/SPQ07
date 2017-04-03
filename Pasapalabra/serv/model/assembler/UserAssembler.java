package model.assembler;

import model.User;
import model.DTO.UserDTO;

public class UserAssembler{
	
	private static UserAssembler instance = new UserAssembler();

	private UserAssembler(){}
	
	public static UserAssembler getInstance(){ return instance; }
	
	
	public UserDTO assembleToDTO(User nonDTO) {
		return new UserDTO(nonDTO.getUserName(), 
				nonDTO.getMail(), 
				nonDTO.getProfileImage(), 
				nonDTO.getDOB(), 
				nonDTO.getGames(), nonDTO.getGamesWon(), nonDTO.getGamesLost());
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
