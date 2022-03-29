package controller.server.states;


import controller.server.GameController;
import exceptions.LobbyNotFoundException;
import util.CharacterType;

/**
 *  When the client activates a character effect, the event handler will call one of the methods Below.
 *  All the information needed will be sent using the parameters.
 *  After the effect is activated, effectIsUsed will become TRUE.
 *  TODO : switch effectIsUsed to FALSE at the end of the turn.
 */
public class CharacterEffectHandler {
    public CharacterEffectHandler(){}

    public void monkEffect(String code, int studentID){
        try{
            GameController.getLobby(code).getModel().getCharacterByType(CharacterType.MONK);
        }catch(LobbyNotFoundException e){

        }

    }
}
