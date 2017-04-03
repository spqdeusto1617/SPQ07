package model.assembler;

import model.Question;
import model.DTO.QuestionDTO;

public class QuestionAssembler{
	
	private static QuestionAssembler instance = new QuestionAssembler();

	private QuestionAssembler(){}
	
	public static QuestionAssembler getInstance(){ return instance; }
	
	
	public QuestionDTO assembleToDTO(Question nonDTO) {
		return new QuestionDTO(nonDTO.isAnswered(),nonDTO.getQuestion(),
				nonDTO.getLeter(),nonDTO.getCreator());
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
