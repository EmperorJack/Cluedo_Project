package cluedo.actions;

import java.util.ArrayList;
import java.util.List;

import cluedo.tokens.Token;

public class MoveSequence implements Action{
	List<BoardMove> sequence;
	BoardMove current;
	Token t;
	public MoveSequence(MoveAction action, Token t){
		sequence = new ArrayList<BoardMove>();
		current = action;
		this.t = t;
	}
	
	public void addAction(BoardMove action){
		sequence.add(action);
	}
	
	public void tick(){
		if(!isFinished()){
			if (!current.isFinished()){
				current.tick(t);
			} else {
				current = getNext();
			}
		}
	}
	
	private BoardMove getNext() {
		BoardMove m = sequence.get(0);
		sequence.remove(m);
		return m;
	}

	public boolean isFinished(){
		return (current.isFinished() && sequence.isEmpty());
	}

}
