package Tree;

public class MainNode extends Node{
    private final BlockNode block;

    public MainNode(BlockNode block) {
        this.block = block;
    }

    @Override
    public void setParent(BlockNode parent) {
        this.parent = parent;
        block.setParent(parent);
    }

    @Override
    public boolean run() {
        return block.run();
    }

    @Override
    protected void print(int level) {
        for(int i = 0; i < level; i++)
            System.out.print("\t");
        System.out.println("Main");
        block.print(level + 1);
    }
}
