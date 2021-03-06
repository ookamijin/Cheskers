package com.ookamijin.cheskers;

import com.ookamijin.framework.Input.TouchEvent;

public class Board {

	// these are the centers of the squares
	public static int x[] = { 200, 280, 360, 440, 520, 600 };
	public static int y[] = { 40, 120, 200, 280, 360, 440 };

	private Tile mTile[][];

	private ChipYellow yellowChips[] = new ChipYellow[16];
	private ChipRed redChips[] = new ChipRed[16];

	public Board() {

		mTile = new Tile[6][6];
		int tNum = 0;

		// populates the tile array with coordinate information
		for (int j = 0; j < 6; ++j) {
			for (int i = 0; i < 6; ++i) {
				mTile[i][j] = new Tile(x[i], y[j]);
			}
		}

		// initial board layout colors
		for (int j = 0; j < 6; ++j) {
			for (int i = 0; i < 6; ++i) {

				switch (j) {
				case 0:
				case 2:
				case 4:
					mTile[i][j].setHasYellow(true);

					break;
				case 1:
				case 3:
				case 5:
					mTile[i][j].setHasRed(true);
					break;
				}

				if (j == 2 && i == 2 || j == 2 && i == 3 || j == 3 && i == 2
						|| j == 3 && i == 3) {
					mTile[i][j].setHasNothing(true);
				}
			}
		}

		// initialize chip ids
		int yellow = 0;
		int red = 0;
		for (int j = 0; j < 6; ++j) {
			for (int i = 0; i < 6; ++i) {

				if (mTile[i][j].hasYellow()) {
					mTile[i][j].setChipIndex(yellow);
					++yellow;
				}
				if (mTile[i][j].hasRed()) {
					mTile[i][j].setChipIndex(red);
					++red;
				}
			}
		}

		// set bonus squares
		mTile[1][1].setBonusYellow(true);
		mTile[2][1].setBonusYellow(true);
		mTile[4][2].setBonusYellow(true);
		mTile[4][3].setBonusYellow(true);
		mTile[1][4].setBonusYellow(true);
		mTile[2][4].setBonusYellow(true);

		mTile[3][1].setBonusRed(true);
		mTile[4][1].setBonusRed(true);
		mTile[1][2].setBonusRed(true);
		mTile[1][3].setBonusRed(true);
		mTile[4][4].setBonusRed(true);
		mTile[3][4].setBonusRed(true);

		// initialize chips (taken from game screen)
		for (int i = 0; i < 16; ++i) {
			yellowChips[i] = new ChipYellow(Board.topInitX(i),
					Board.topInitY(i), i);

			redChips[i] = new ChipRed(Board.botInitX(i), Board.botInitY(i), i);
		}
	}

	public void update() {
		for (int i = 0; i < 16; ++i) {
			yellowChips[i].update();

			redChips[i].update();
		}
	}

	public static int topInitX(int index) {
		return botInitX(index);
	}

	public static int botInitX(int index) {

		switch (index) {
		case 0:
		case 6:
		case 10:
			return x[0];
		case 1:
		case 7:
		case 11:
			return x[1];
		case 2:
		case 12:
			return x[2];
		case 3:
		case 13:
			return x[3];
		case 4:
		case 8:
		case 14:
			return x[4];
		case 5:
		case 9:
		case 15:
			return x[5];

		}

		return -1;
	}

	private static int getY(int index) {

		// calculates topInitIndex. add one for bottom
		if (index < 6) {
			return 0;
		} else if (index < 10) {
			return 2;
		} else {
			return 4;
		}
	}

	public static int topInitY(int index) {
		return y[getY(index)];
	}

	public static int botInitY(int index) {
		return y[getY(index) + 1];

	}

	public Coord getTileIndex(TouchEvent event) {

		Coord coords = new Coord(-1, -1);
		for (int j = 0; j < 6; ++j) {
			for (int i = 0; i < 6; ++i) {
				if (mTile[i][j].inBounds(event)) {
					coords.setX(i);
					coords.setY(j);
					return coords;
				}
			}
		}
		return coords;
	}

	public Coord getTileCenter(Coord coord) {
		Coord chipPos = new Coord(x[coord.x], y[coord.y]);
		return chipPos;
	}

	public boolean tileIsEmpty(Coord coord) {

		return mTile[coord.x][coord.y].hasNothing();
	}

	public boolean tileHasRed(Coord coord) {

		return mTile[coord.x][coord.y].hasRed();
	}

	public boolean tileHasYellow(Coord coord) {
        return mTile[coord.x][coord.y].hasYellow();
	}

	public void setTileHasNothing(Coord coord) {
		mTile[coord.x][coord.y].setHasNothing(true);
		mTile[coord.x][coord.y].setChipIndex(0);
	}

	public void setTileHasYellow(Coord coord) {
		mTile[coord.x][coord.y].setHasYellow(true);

	}

	public void setTileHasRed(Coord coord) {
		mTile[coord.x][coord.y].setHasRed(true);

	}

	public void setTileChipIndex(Coord coord, int id) {
		mTile[coord.x][coord.y].setChipIndex(id);

	}

	/**
	 * 
	 * @param coord
	 * @return index of chip at tile coord
	 */
	public int getTileChipIndex(Coord coord) {
		return mTile[coord.x][coord.y].getChipIndex();

	}

	public String tileStatus(Coord coord) {
		String status = "";

		if (mTile[coord.x][coord.y].hasNothing()) {
			status += "N";
		} else if (mTile[coord.x][coord.y].hasRed()) {
			status += "R";
		} else {
			status += "Y";
		}

		if (mTile[coord.x][coord.y].getChipIndex() < 10)
			status += "0";
		status += mTile[coord.x][coord.y].getChipIndex();
		return status;
	}

	public Tile getTile(Coord coord) {
		return mTile[coord.x][coord.y];
	}

	public boolean tileHasNothing(Coord coord) {
		return mTile[coord.x][coord.y].hasNothing();
	}

	public Chip getChip(Coord coord) {
		if (tileHasRed(coord)) {
			return redChips[mTile[coord.x][coord.y].getChipIndex()];
		}
		return yellowChips[mTile[coord.x][coord.y].getChipIndex()];
	}

	public int getYellowChipX(int i) {
		return yellowChips[i].getCenterX();
	}

	public int getYellowChipY(int i) {
		return yellowChips[i].getCenterY();
	}

	public int getRedChipX(int i) {
		return redChips[i].getCenterX();
	}

	public int getRedChipY(int i) {
		return redChips[i].getCenterY();
	}

	public boolean tileHasMatch(Coord coord, Chip objectChip) {
		return mTile[coord.x][coord.y].matches(objectChip);

	}

	public boolean tileHasOpposite(Coord coord, Chip objectChip) {
		return mTile[coord.x][coord.y].opposite(objectChip);
	}

	public boolean tileIsBonusYellow(Coord coord) {
		return mTile[coord.x][coord.y].isBonusYellow();
	}
	
	public boolean tileIsBonusRed(Coord coord) {
		return mTile[coord.x][coord.y].isBonusRed();
	}

}
