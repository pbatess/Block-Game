package assignment3;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;


public class Block {
 private int xCoord;
 private int yCoord;
 private int size; // height/width of the square
 private int level; // the root (outer most block) is at level 0
 private int maxDepth;
 private Color color;

 private Block[] children; // {UR, UL, LL, LR}

 public static Random gen = new Random(4);


 /*
  * These two constructors are here for testing purposes.
  */
 public Block() {}

 public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
  this.xCoord=x;
  this.yCoord=y;
  this.size=size;
  this.level=lvl;
  this.maxDepth = maxD;
  this.color=c;
  this.children = subBlocks;
 }



 /*
  * Creates a random block given its level and a max depth.
  *
  * xCoord, yCoord, size, and highlighted should not be initialized
  * (i.e. they will all be initialized by default)
  */
 public Block(int lvl, int maxDepth){
  this.level = lvl;
  this.maxDepth = maxDepth;
  this.color = null;
  this.children = new Block[0];
  Block[] children = new Block[4];

  if (lvl > maxDepth){
   throw new IllegalArgumentException("Invalid level.");
  }

  if (lvl != maxDepth){

   double rand = gen.nextDouble();

   if (rand < Math.exp(-0.25 * level)) {

    for (int i = 0; i < 4; i++) {

     children[i] = new Block(lvl + 1, maxDepth);
     // recursively calls the function to make children arrays

    }
    this.children = children;
    //reassigns children to filled array

   }
   else{
    color = GameColors.BLOCK_COLORS[gen.nextInt(4)];
   }
  }

  else{
   color = GameColors.BLOCK_COLORS[gen.nextInt(4)];
  }
 }


 /*
  * Updates size and position for the block and all of its sub-blocks, while
  * ensuring consistency between the attributes and the relationship of the
  * blocks.
  *
  *  The size is the height and width of the block. (xCoord, yCoord) are the
  *  coordinates of the top left corner of the block.
  */
 public void updateSizeAndPosition (int size, int xCoord, int yCoord) {

  if (size <= 0 || (size % 2 != 0 && level != maxDepth)) {
   // throws argument is size less than/equal to 0 or if it's not divisible by 2, excluding when the size = 1 and
   // the level is at its maxDepth.
   throw new IllegalArgumentException("Size is invalid.");
  }
  this.size = size;
  this.xCoord = xCoord;
  this.yCoord = yCoord;

  if (this.children.length != 0) {
   // updates each child's coordinate if the block already has children

   this.children[0].updateSizeAndPosition(size / 2, this.xCoord + size / 2, yCoord);
   this.children[1].updateSizeAndPosition(size / 2, xCoord, yCoord);
   this.children[2].updateSizeAndPosition(size / 2, xCoord, this.yCoord + size / 2);
   this.children[3].updateSizeAndPosition(size / 2, this.xCoord + size / 2, this.yCoord + size / 2);

  }
 }



 /*
  * Returns a List of blocks to be drawn to get a graphical representation of this block.
  *
  * This includes, for each undivided Block:
  * - one BlockToDraw in the color of the block
  * - another one in the FRAME_COLOR and stroke thickness 3
  *
  * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
  *
  * The order in which the blocks to draw appear in the list does NOT matter.
  */
 public ArrayList<BlockToDraw> getBlocksToDraw() {
  BlockToDraw block = new BlockToDraw(this.color, this.xCoord, this.yCoord, this.size, 0);
  BlockToDraw color = new BlockToDraw(GameColors.FRAME_COLOR, this.xCoord, this.yCoord, this.size, 3);
  ArrayList<BlockToDraw> Blocks = new ArrayList<BlockToDraw>();

  if (this.children.length == 0){
   // if block has no children, add its color to array
   Blocks.add(block);
   Blocks.add(color);
  }
  else {
   for (int i = 0; i < 4; i++){
    // adds each child to the array
    Blocks.addAll(this.children[i].getBlocksToDraw());
   }
  }
  return Blocks;
 }

 /*
  * This method is provided and you should NOT modify it.
  */
 public BlockToDraw getHighlightedFrame() {
  return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
 }



 /*
  * Return the Block within this Block that includes the given location
  * and is at the given level. If the level specified is lower than
  * the lowest block at the specified location, then return the block
  * at the location with the closest level value.
  *
  * The location is specified by its (x, y) coordinates. The lvl indicates
  * the level of the desired Block. Note that if a Block includes the location
  * (x, y), and that Block is subdivided, then one of its sub-Blocks will
  * contain the location (x, y) too. This is why we need lvl to identify
  * which Block should be returned.
  *
  * Input validation:
  * - this.level <= lvl <= maxDepth (if not throw exception)
  * - if (x,y) is not within this Block, return null.
  */
 public Block getSelectedBlock(int x, int y, int lvl) {
  if (this.level > lvl || lvl > this.maxDepth) {
   throw new IllegalArgumentException("Invalid level.");
  }
  Block found = new Block();

  if ((x >= this.xCoord && this.xCoord + this.size >= x) && (y >= this.yCoord && y <= this.yCoord + this.size)) {
   // if the x and y coordinate are in between the current blocks x and y coordinates
   found = this;
   // current block is the return value
   if (this.level == lvl || this.children.length == 0){
    // return block if the level matches
    return found;
   }

   else{
    // else check the children
    for (int j = 0; j < 4; j++) {
     found = this.children[j].getSelectedBlock(x, y, lvl);
     if (found != null) {
      // returns matched child if not null
      return found;
     }
    }
   }
  }
  return null;
 }








 /*
  * Swaps the child Blocks of this Block.
  * If input is 1, swap vertically. If 0, swap horizontally.
  * If this Block has no children, do nothing. The swap
  * should be propagate, effectively implementing a reflection
  * over the x-axis or over the y-axis.
  *
  */
 public void reflect(int direction) {
  if (direction != 0 && direction != 1){
   throw new IllegalArgumentException("Invalid input.");
  }

  if(direction == 0){

   if (this.children.length != 0){
    // checks if there are children to reflect
    Block[] NewChildren = {this.children[3], this.children[2], this.children[1], this.children[0]};
    // rearranges children to reflect the x-axis
    this.children = NewChildren;

    for (int i = 0; i < 4; i++){
     // iterates children to update their coordinates
     Block child = this.children[i];

     if (i > 1) {
      child.updateSizeAndPosition(child.size, child.xCoord,child.yCoord + child.size);
     }
     else{
      child.updateSizeAndPosition(child.size, child.xCoord, child.yCoord - child.size);
     }
     child.reflect(direction);
     // rotates the children of this child block
    }
   }
  }
  if (direction == 1){

   if (this.children.length != 0){
    Block[] NewChildren = {this.children[1], this.children[0], this.children[3], this.children[2]};
    // rearranges children to reflect the y-axis
    this.children = NewChildren;

    for (int s = 0; s < 4; s++){
     Block child = this.children[s];

     if (s == 1 || s == 2){
      child.updateSizeAndPosition(child.size, child.xCoord - child.size, child.yCoord);
     }
     else{
      child.updateSizeAndPosition(child.size, child.xCoord + child.size, child.yCoord);
     }
     child.reflect(direction);
    }
   }
  }
 }



 /*
  * Rotate this Block and all its descendants.
  * If the input is 1, rotate clockwise. If 0, rotate
  * counterclockwise. If this Block has no children, do nothing.
  */
 public void rotate(int direction) {
  if (direction != 1 && direction != 0) {
   throw new IllegalArgumentException("Invalid input.");
  }

  if (direction == 0) {
   if (this.children.length != 0) {

    Block[] NewChildren = {this.children[3], this.children[0], this.children[1], this.children[2]};
    // rearrange children to rotate counter-clockwise
    this.children = NewChildren;

    for (int i = 0; i < 4; i++) {
     // iterates through each child and updates x and y coordinate

     Block child = this.children[i];

     if (i == 0) {
      child.updateSizeAndPosition(child.size, child.xCoord, child.yCoord - child.size);
     } else if (i == 1) {
      child.updateSizeAndPosition(child.size, child.xCoord - child.size, child.yCoord);
     } else if (i == 2) {
      child.updateSizeAndPosition(child.size, child.xCoord, child.yCoord + child.size);
     } else {
      child.updateSizeAndPosition(child.size, child.xCoord + child.size, child.yCoord);
     }
     child.rotate(direction);
    }
   }
  }


  if (direction == 1) {
   if (this.children.length != 0) {

    Block[] NewChildren = {this.children[1], this.children[2], this.children[3], this.children[0]};
    // rearrange children to rotate clockwise
    this.children = NewChildren;
    for (int y = 0; y <4 ; y++){
     this.children[y].printBlock();
    }


    for (int s = 0; s < 4; s++) {
     // iterates each child and updates x and y coordinate
     Block child = this.children[s];

     if (s == 0) {
      child.updateSizeAndPosition(child.size, child.xCoord + child.size, child.yCoord);
     } else if (s == 1) {
      child.updateSizeAndPosition(child.size, child.xCoord, child.yCoord - child.size);
     } else if (s == 2) {
      child.updateSizeAndPosition(child.size, child.xCoord - child.size, child.yCoord);
     } else {
      child.updateSizeAndPosition(child.size, child.xCoord, child.yCoord + child.size);
     }
     child.rotate(direction);
    }
   }
  }

 }



 /*
  * Smash this Block.
  *
  * If this Block can be smashed,
  * randomly generate four new children Blocks for it.
  * (If it already had children Blocks, discard them.)
  * Ensure that the invariants of the Blocks remain satisfied.
  *
  * A Block can be smashed iff it is not the top-level Block
  * and it is not already at the level of the maximum depth.
  *
  * Return True if this Block was smashed and False otherwise.
  *
  */
 public boolean smash(){
  if (this.level < maxDepth && this.level > 0){

   this.color = null;
   this.children = new Block[4];
   // clears current blocks color and children

   for (int i = 0; i < 4; i++) {
    // iterates 4 times to create new children blocks
    if (this.level + 1 < this.maxDepth){
     // checks if next level is less than the maxDepth to prevent new blocks from being created past the maximum depth allowed
     this.children[i] = new Block(this.level + 1, this.maxDepth);
    }
    else {
     this.children[i] = new Block(this.level, this.level);
    }
   }

   this.updateSizeAndPosition(this.size, this.xCoord, this.yCoord);
   // updates the new blocks size, x and y coordinates
   return true;
  }

  return false;
 }


 /*
  * Return a two-dimensional array representing this Block as rows and columns of unit cells.
  *
  * Return and array arr where, arr[i] represents the unit cells in row i,
  * arr[i][j] is the color of unit cell in row i and column j.
  *
  * arr[0][0] is the color of the unit cell in the upper left corner of this Block.
  */
 public Color[][] flatten() {
  int axis = (int) Math.pow(2,this.maxDepth);
  ArrayList<BlockToDraw> colors = this.getBlocksToDraw();
  int totalSize = this.size;
  int maxdepth = this.maxDepth;
  // stores current blocks size and maxDepth

  Color[][] Color = new Color[axis][axis];
  if (this.children.length == 0){
   // if the current block has no children, fill entire array with that color.
   for(int j = 0; j < Color.length; j++){
    for (int i = 0; i < Color.length; i++){
     Color[i][j] = this.color;
    }
   }
  }

  for (BlockToDraw b : colors){
   if (b.getColor() != GameColors.FRAME_COLOR){
    // if block has color

    int scalar = totalSize;
    for (int s = 0; s < maxdepth; s++){
     // divides the biggest block's size maxDepth number of times to find the scalar
     scalar = scalar/2;
    }
    int x = b.getShape().x / scalar;
    int y = b.getShape().y / scalar;
    int times = b.getShape().height / scalar;
    // find x, y and how far across the block goes across/down (times) in relation to the array

    for (int j = (int) x; j < x + times; ++j) {

     for (int i = (int) y; i < y + times; ++i) {
      // iterates x and y axis the number of times the current blocks color has to go across (starting from x and y position)
      Color[i][j] = b.getColor();
     }
    }
   }
  }
  return Color;
 }



 // These two get methods have been provided. Do NOT modify them.
 public int getMaxDepth() {
  return this.maxDepth;
 }

 public int getLevel() {
  return this.level;
 }


 /*
  * The next 5 methods are needed to get a text representation of a block.
  * You can use them for debugging. You can modify these methods if you wish.
  */
 public String toString() {
  return String.format("pos=(%d,%d), size=%d, level=%d"
          , this.xCoord, this.yCoord, this.size, this.level);
 }

 public void printBlock() {
  this.printBlockIndented(0);
 }

 private void printBlockIndented(int indentation) {
  String indent = "";
  for (int i=0; i<indentation; i++) {
   indent += "\t";
  }

  if (this.children.length == 0) {
   // it's a leaf. Print the color!
   String colorInfo = GameColors.colorToString(this.color) + ", ";
   System.out.println(indent + colorInfo + this);
  } else {
   System.out.println(indent + this);
   for (Block b : this.children)
    b.printBlockIndented(indentation + 1);
  }
 }

 private static void coloredPrint(String message, Color color) {
  System.out.print(GameColors.colorToANSIColor(color));
  System.out.print(message);
  System.out.print(GameColors.colorToANSIColor(Color.WHITE));
 }

 public void printColoredBlock(){
  Color[][] colorArray = this.flatten();
  for (Color[] colors : colorArray) {
   for (Color value : colors) {
    String colorName = GameColors.colorToString(value).toUpperCase();
    if(colorName.length() == 0){
     colorName = "\u2588";
    }else{
     colorName = colorName.substring(0, 1);
    }
    coloredPrint(colorName, value);
   }
   System.out.println();
  }
 }

}
