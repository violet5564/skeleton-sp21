package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author Shabriri
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        board.setViewingPerspective(side);
        for(int column = 0; column < board.size(); column++){
            changed |= handleColumn(column);

        }
        board.setViewingPerspective(Side.NORTH);
        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

//        for (int c = 0; c < board.size(); c += 1) {
//            for (int r = 0; r < board.size(); r += 1) {
//                Tile t = board.tile(c, r);
//                if (board.tile(c, r) != null) {
//                    board.move(c, 3, t);
//                    changed = true;
//                    score += 7;
//                }
//            }
//        }

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }
    /** 只处理“向北”倾斜的一列 c（调用前已 setViewingPerspective(side)）。 */
    private boolean handleColumn(int c) {
        boolean changed = false;
        int N = board.size();
        int target = N - 1;        // 下一次要填充/合并的位置（从顶开始）
        int lastMergedRow = -1;    // 本轮已经合并过的行，避免二次合并

        for (int r = N - 1; r >= 0; r--) {
            Tile t = board.tile(c,r); // t为当前等待操作的tile

            if(t == null){
                continue;
            }
            if(r == target){
                continue;
            }

            Tile atTarget = board.tile(c,target);// atTarget为将要移动或merge的目标tile

            if(atTarget == null){
                board.move(c,target,t);
                changed = true;
                // 此时不去动target，后面可能有合并的操作
            }else if(target !=lastMergedRow && t.value() == atTarget.value()){
                int mergeValue = atTarget.value()*2;
                boolean move = board.move(c,target,t);
                changed |= move;
                this.score += mergeValue;
                lastMergedRow = target; // 防止merge两次
                target--;
            }else{
                target--;
                if(target != r) {
//                    boolean move = board.move(c, target, t);
//                    changed |= move;
                    board.move(c, target, t);
                    changed = true;
                }

            }
        }
        return changed;
    }

//    private boolean handleColumn(int column){
//        boolean changed = false;
//        for(int row = board.size() -1 ; row >= 0; row -= 1){
//            if(board.tile(column,row) != null){
//                for(int r = row -1; r >=0; r -= 1){
//                    Tile t = board.tile(column,r);
//                    if(t != null){
//                        if(t.value() == board.tile(column,row).value()){
//                            // merge tile
//                            board.move(column, row,t);
//                            // 修改t.value()
//                            // 加分
//                            changed = true;
//                            break; // move to the next tile after merge
//                        }else{
//                            break; // Can't merge, move to the next tile
//                        }
//                    }
//                }
//            }else{
//                for(int r = row -1; r >=0; r -= 1){
//                    Tile t = board.tile(column,r);
//                    if(t != null){
//                        board.move(column, row,t);
//                        changed = true;
//                        break; // move to next tile after move
//                    }
//                }
//            }
//        }
//        return changed;
//    }
//
    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        // return true if any of the tiles in the given board are null.
        for(int i = 0; i < b.size(); i ++) {
            for (int j = 0; j < b.size(); j++) {
                if (b.tile(i, j) == null) return true;
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        for(int i = 0; i < b.size(); i ++) {
            for (int j = 0; j < b.size(); j++) {
                if (b.tile(i, j) != null) {
                    if(b.tile(i,j).value() == 2048){
                        return true;
                    }
                }

            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        // 1. t == null
        // 2. t(i,j).value() == t(i, j+1).value() or t(i,j).value = t(i+1,j).value()
        if(emptySpaceExists(b)) return true;
        int N = b.size();
//        for(int i = 0; i < b.size(); i++){
//            for(int j = 0; j<b.size(); j++){
//                if(i != b.size()){
//                    if(b.tile(i,j).value() == b.tile(i+1,j).value()){
//                        return true;
//                    }
//                }
//                if(j != b.size()){
//                    if(b.tile(i,j).value() == b.tile(i,j+1).value()){
//                        return true;
//                    }
//                }
//            }
//        }
        for(int c = 0; c <N-1; c++){
            for(int r = 3; r>0 ; r--){
                Tile t = b.tile(c,r);
                if(t.value() == b.tile(c,r-1).value() || t.value() == b.tile(c+1,r).value()){
                    return true;
                }
            }
        }
        return false;
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
