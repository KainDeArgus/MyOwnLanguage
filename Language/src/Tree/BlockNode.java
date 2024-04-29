package Tree;

import Tokens.TOKENS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BlockNode extends Node{
    Map<String, Integer> identInt = new HashMap<>();
    Map<String, Float> identFloat = new HashMap<>();
    Map<String, String> identString = new HashMap<>();
    Map<String, Character> identChar = new HashMap<>();
    //Словари с переменными в области видимости данного блока
    protected final ArrayList<Node> nodes;

    public BlockNode(ArrayList<Node> nodes) {
        if(nodes != null) {
            this.nodes = new ArrayList<>(nodes);
            for (Node n : nodes)
                n.setParent(this);
        }
        else
            this.nodes = null;
    }
    @Override
    public boolean run(){
        if(nodes == null)
            return false;
        for(Node n : nodes)
            if(!n.run())
                return false;

        return true;
    }

    @Override
    protected void print(int level) {
        for(int i = 0; i < level; i++)
            System.out.print("\t");
        System.out.println("Block");
        if(nodes != null)
            for(Node n : nodes)
                n.print(level + 1);
    }

    protected Iterator<Node> getNodes(){
        return nodes == null ? null : nodes.iterator();
    }

    protected void makeIdent(String nameIdent, char valueIdent) {
        identChar.put(nameIdent, valueIdent);
    }
    public void makeIdent(String nameIdent, int valueIdent) {
        identInt.put(nameIdent, valueIdent);
    }
    public void makeIdent(String nameIdent, float valueIdent) {
        identFloat.put(nameIdent, valueIdent);
    }
    public void makeIdent(String nameIdent, String valueIdent) {
        identString.put(nameIdent, valueIdent);
    }
    protected boolean setIdent(String nameIdent, char valueIdent) {
        if(identChar.containsKey(nameIdent)) {
            identChar.put(nameIdent, valueIdent);
            return true;
        }
        else
            return parent != null && parent.setIdent(nameIdent, valueIdent);
    }
    public boolean setIdent(String nameIdent, int valueIdent) {
        if(identInt.containsKey(nameIdent)) {
            identInt.put(nameIdent, valueIdent);
            return true;
        }
        else
            return parent != null && parent.setIdent(nameIdent, valueIdent);
    }
    public boolean setIdent(String nameIdent, float valueIdent) {
        if(identFloat.containsKey(nameIdent)) {
            identFloat.put(nameIdent, valueIdent);
            return true;
        }
        else
            return parent != null && parent.setIdent(nameIdent, valueIdent);
    }
    public boolean setIdent(String nameIdent, String valueIdent) {
        if(identString.containsKey(nameIdent)) {
            identString.put(nameIdent, valueIdent);
            return true;
        }
        else
            return parent != null && parent.setIdent(nameIdent, valueIdent);
    }
    protected Character getIdent(String nameIdent, char type) {
        if(identChar.containsKey(nameIdent))
            return identChar.get(nameIdent);
        else
            return parent == null ? null : parent.getIdent(nameIdent, Character.MAX_VALUE);
    }
    public Integer getIdent(String nameIdent, int type) {
        if(identInt.containsKey(nameIdent))
            return identInt.get(nameIdent);
        else
            return parent == null ? null : parent.getIdent(nameIdent, Integer.MAX_VALUE);
    }
    public Float getIdent(String nameIdent, float type) {
        if(identFloat.containsKey(nameIdent))
            return identFloat.get(nameIdent);
        else
            return parent == null ? null : parent.getIdent(nameIdent, Float.MAX_VALUE);
    }
    public String getIdent(String nameIdent, String type) {
        if(identString.containsKey(nameIdent))
            return identString.get(nameIdent);
        else
            return parent == null ? null : parent.getIdent(nameIdent, "");
    }
    protected TOKENS findIdent(String nameIdent){
        TOKENS finding = null;
        for(int i = 0; i < 5 && finding == null; i++)
            switch (i){
                case 0 -> finding = identInt.containsKey(nameIdent) ? TOKENS.T_INT : null;
                case 1 -> finding = identFloat.containsKey(nameIdent) ? TOKENS.T_FLOAT : null;
                case 2 -> finding = identString.containsKey(nameIdent) ? TOKENS.T_STRING : null;
                case 3 -> finding = identChar.containsKey(nameIdent) ? TOKENS.T_CHAR : null;
                case 4 -> finding = parent != null ? parent.findIdent(nameIdent) : null;
            }
        return finding;
    }
}