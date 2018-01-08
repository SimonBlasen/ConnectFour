package util;

import de.cogsys.ai.sogo.game.SogoGame;


/*
 * Class containing database for heuristic and finishing moves (winning constellations, killer moves and dilemmas
 */
public class GameAnalyzer {

	    // lines that contain a win
		public static final long[] longLines = new long[] {
				0x000000000000000FL, //0
				0x00000000000000F0L,
				0x0000000000000F00L,
				0x000000000000F000L,
				0x00000000000F0000L,
				0x0000000000F00000L,
				0x000000000F000000L,
				0x00000000F0000000L,
				0x0000000F00000000L,
				0x000000F000000000L,
				0x00000F0000000000L,
				0x0000F00000000000L,
				0x000F000000000000L,
				0x00F0000000000000L,
				0x0F00000000000000L,
				0xF000000000000000L, //15

				0x0000000000001111L, //16
				0x0000000000002222L,
				0x0000000000004444L,
				0x0000000000008888L,
				0x0000000011110000L,
				0x0000000022220000L,
				0x0000000044440000L,
				0x0000000088880000L,
				0x0000111100000000L,
				0x0000222200000000L,
				0x0000444400000000L,
				0x0000888800000000L,
				0x1111000000000000L,
				0x2222000000000000L,
				0x4444000000000000L,
				0x8888000000000000L, //31

				0x0001000100010001L, //32
				0x0002000200020002L,
				0x0004000400040004L,
				0x0008000800080008L,
				0x0010001000100010L,
				0x0020002000200020L,
				0x0040004000400040L,
				0x0080008000800080L,
				0x0100010001000100L,
				0x0200020002000200L,
				0x0400040004000400L,
				0x0800080008000800L,
				0x1000100010001000L,
				0x2000200020002000L,
				0x4000400040004000L,
				0x8000800080008000L, //47

				0x0008000400020001L,
				0x0080004000200010L,
				0x0800040002000100L,
				0x8000400020001000L,
				0x0001000200040008L,
				0x0010002000400080L,
				0x0100020004000800L,
				0x1000200040008000L,

				0x1000010000100001L,
				0x2000020000200002L,
				0x4000040000400004L,
				0x8000080000800008L,
				0x0001001001001000L,
				0x0002002002002000L,
				0x0004004004004000L,
				0x0008008008008000L,

				0x0000000000008421L,
				0x0000000084210000L,
				0x0000842100000000L,
				0x8421000000000000L,
				0x0000000000001248L,
				0x0000000012480000L,
				0x0000124800000000L,
				0x1248000000000000L,

				0x8000040000200001L,
				0x0001002004008000L,
				0x1000020000400008L,
				0x0008004002001000L,
				};
		
		

		private static long[] oneWallKillerBoardsP1 = new long[]
				{
						// Der 9er Move
						0x9000B, 0xB000BL, 0x1000B000BL, 0x2000B000BL, 0x8000B000BL, 0xA000B000BL, 0x9000B000BL, 0x3000B000BL, 0xA000B000BL, 0x9000B000BL, 0x3000B000BL, 0xB000B000BL, 0xB000B000BL, 0xB000B000BL, 0xF000B000BL, 0x8000B000B000BL, 0x2000B000B000BL, 0x1000B000B000BL, 
						// Der 7er Killer v1
						0x40006000CL, 0x4000E000CL, 0xC000E000CL, 0xE000E000CL, 0x1000E000E000CL, 0xF000E000CL, 
						// Der 7er Killer v2
						0x40006000DL, 0xC0006000DL, 0xE0006000DL, 0xF0006000DL, 0x1000E0006000DL, 
				};
		private static long[] oneWallKillerBoardsP2 = new long[]
				{
					0xF000F00060004L, 0xF000F00040000L, 0xF000E00000000L, 0xF000D00000000L, 0xF000700000000L, 0x7000500000000L, 0x7000600000000L, 0xE000C00000000L, 0xD000500000000L, 0xE000600000000L, 0xD000C00000000L, 0xC000400000000L, 0x6000400000000L, 0x5000400000000L, 0x0L, 0x0L, 0x0L, 0x0L, 
					0xF000B00090000L, 0xB000B00010000L, 0xB000300000000L, 0x3000100000000L, 0x0L, 0x0L, 
					0xF000B00010000L, 0x3000300010000L, 0x1000100000000L, 0x0L, 0x0L, 
				};
		
		
		public static long[] killerBoardsP1 = new long[]
				{
						
						
				};
		public static long[] killerBoardsP2 = new long[]
				{
						
						
				};

		
		private static void fillKillerBoards()
		{
			killerBoardsP1 = new long[oneWallKillerBoardsP1.length * 20];
			killerBoardsP2 = new long[oneWallKillerBoardsP1.length * 20];
			for (int i = 0; i < oneWallKillerBoardsP1.length; i++)
			{
				killerBoardsP1[i * 20 + 0] = oneWallKillerBoardsP1[i];
				killerBoardsP2[i * 20 + 0] = oneWallKillerBoardsP2[i];

				long p1f0 = 0xFL & oneWallKillerBoardsP1[i];
				long p1f1 = (0xF0000L & oneWallKillerBoardsP1[i]) >> 16;
				long p1f2 = (0xF00000000L & oneWallKillerBoardsP1[i]) >> 32;
				long p1f3 = (0xF000000000000L & oneWallKillerBoardsP1[i]) >> 48;
				long p2f0 = 0xFL & oneWallKillerBoardsP2[i];
				long p2f1 = (0xF0000L & oneWallKillerBoardsP2[i]) >> 16;
				long p2f2 = (0xF00000000L & oneWallKillerBoardsP2[i]) >> 32;
				long p2f3 = (0xF000000000000L & oneWallKillerBoardsP2[i]) >> 48;
				
				p1f0 = mirrorSingle(p1f0);
				p1f1 = mirrorSingle(p1f1);
				p1f2 = mirrorSingle(p1f2);
				p1f3 = mirrorSingle(p1f3);
				p2f0 = mirrorSingle(p2f0);
				p2f1 = mirrorSingle(p2f1);
				p2f2 = mirrorSingle(p2f2);
				p2f3 = mirrorSingle(p2f3);

				killerBoardsP1[i * 20 + 1] = p1f0 | (p1f1 << 16) | (p1f2 << 32) | (p1f3 << 48);
				killerBoardsP2[i * 20 + 1] = p2f0 | (p2f1 << 16) | (p2f2 << 32) | (p2f3 << 48);

				
				p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

				killerBoardsP1[i * 20 + 2] = (p1f0 << (4)) | (p1f1 << (16 + 4)) | (p1f2 << (32 + 4)) | (p1f3 << (48 + 4));
				killerBoardsP2[i * 20 + 2] = (p2f0 << (4)) | (p2f1 << (16 + 4)) | (p2f2 << (32 + 4)) | (p2f3 << (48 + 4));

				p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

				killerBoardsP1[i * 20 + 3] = (p1f0 << (4)) | (p1f1 << (16 + 4)) | (p1f2 << (32 + 4)) | (p1f3 << (48 + 4));
				killerBoardsP2[i * 20 + 3] = (p2f0 << (4)) | (p2f1 << (16 + 4)) | (p2f2 << (32 + 4)) | (p2f3 << (48 + 4));
				
				
				p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

				killerBoardsP1[i * 20 + 4] = (p1f0 << (8)) | (p1f1 << (16 + 8)) | (p1f2 << (32 + 8)) | (p1f3 << (48 + 8));
				killerBoardsP2[i * 20 + 4] = (p2f0 << (8)) | (p2f1 << (16 + 8)) | (p2f2 << (32 + 8)) | (p2f3 << (48 + 8));

				p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

				killerBoardsP1[i * 20 + 5] = (p1f0 << (8)) | (p1f1 << (16 + 8)) | (p1f2 << (32 + 8)) | (p1f3 << (48 + 8));
				killerBoardsP2[i * 20 + 5] = (p2f0 << (8)) | (p2f1 << (16 + 8)) | (p2f2 << (32 + 8)) | (p2f3 << (48 + 8));
			
				
				p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

				killerBoardsP1[i * 20 + 6] = (p1f0 << (12)) | (p1f1 << (16 + 12)) | (p1f2 << (32 + 12)) | (p1f3 << (48 + 12));
				killerBoardsP2[i * 20 + 6] = (p2f0 << (12)) | (p2f1 << (16 + 12)) | (p2f2 << (32 + 12)) | (p2f3 << (48 + 12));

				p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

				killerBoardsP1[i * 20 + 7] = (p1f0 << (12)) | (p1f1 << (16 + 12)) | (p1f2 << (32 + 12)) | (p1f3 << (48 + 12));
				killerBoardsP2[i * 20 + 7] = (p2f0 << (12)) | (p2f1 << (16 + 12)) | (p2f2 << (32 + 12)) | (p2f3 << (48 + 12));


				p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

				killerBoardsP1[i * 20 + 8] = ((0x1L & p1f0) << (0)) | ((0x2L & p1f0) << (4 - 1)) | ((0x4L & p1f0) << (8 - 2)) | ((0x8L & p1f0) << (12 - 3))
						| ((0x1L & p1f1) << (16 + 0)) | ((0x2L & p1f1) << (16 + 4 - 1)) | ((0x4L & p1f1) << (16 + 8 - 2)) | ((0x8L & p1f1) << (16 + 12 - 3))
						| ((0x1L & p1f2) << (32 + 0)) | ((0x2L & p1f2) << (32 + 4 - 1)) | ((0x4L & p1f2) << (32 + 8 - 2)) | ((0x8L & p1f2) << (32 + 12 - 3))
						| ((0x1L & p1f3) << (48 + 0)) | ((0x2L & p1f3) << (48 + 4 - 1)) | ((0x4L & p1f3) << (48 + 8 - 2)) | ((0x8L & p1f3) << (48 + 12 - 3));
				killerBoardsP2[i * 20 + 8] = ((0x1L & p2f0) << (0)) | ((0x2L & p2f0) << (4)) | ((0x4L & p2f0) << (8 - 2)) | ((0x8L & p2f0) << (12 - 3))
						| ((0x1L & p2f1) << (16 + 0)) | ((0x2L & p2f1) << (16 + 4 - 1)) | ((0x4L & p2f1) << (16 + 8 - 2)) | ((0x8L & p2f1) << (16 + 12 - 3))
						| ((0x1L & p2f2) << (32 + 0)) | ((0x2L & p2f2) << (32 + 4 - 1)) | ((0x4L & p2f2) << (32 + 8 - 2)) | ((0x8L & p2f2) << (32 + 12 - 3))
						| ((0x1L & p2f3) << (48 + 0)) | ((0x2L & p2f3) << (48 + 4 - 1)) | ((0x4L & p2f3) << (48 + 8 - 2)) | ((0x8L & p2f3) << (48 + 12 - 3));

				p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

				killerBoardsP1[i * 20 + 9] = ((0x1L & p1f0) << (0)) | ((0x2L & p1f0) << (4 - 1)) | ((0x4L & p1f0) << (8 - 2)) | ((0x8L & p1f0) << (12 - 3))
						| ((0x1L & p1f1) << (16 + 0)) | ((0x2L & p1f1) << (16 + 4 - 1)) | ((0x4L & p1f1) << (16 + 8 - 2)) | ((0x8L & p1f1) << (16 + 12 - 3))
						| ((0x1L & p1f2) << (32 + 0)) | ((0x2L & p1f2) << (32 + 4 - 1)) | ((0x4L & p1f2) << (32 + 8 - 2)) | ((0x8L & p1f2) << (32 + 12 - 3))
						| ((0x1L & p1f3) << (48 + 0)) | ((0x2L & p1f3) << (48 + 4 - 1)) | ((0x4L & p1f3) << (48 + 8 - 2)) | ((0x8L & p1f3) << (48 + 12 - 3));
				killerBoardsP2[i * 20 + 9] = ((0x1L & p2f0) << (0)) | ((0x2L & p2f0) << (4 - 1)) | ((0x4L & p2f0) << (8 - 2)) | ((0x8L & p2f0) << (12 - 3))
						| ((0x1L & p2f1) << (16 + 0)) | ((0x2L & p2f1) << (16 + 4 - 1)) | ((0x4L & p2f1) << (16 + 8 - 2)) | ((0x8L & p2f1) << (16 + 12 - 3))
						| ((0x1L & p2f2) << (32 + 0)) | ((0x2L & p2f2) << (32 + 4 - 1)) | ((0x4L & p2f2) << (32 + 8 - 2)) | ((0x8L & p2f2) << (32 + 12 - 3))
						| ((0x1L & p2f3) << (48 + 0)) | ((0x2L & p2f3) << (48 + 4 - 1)) | ((0x4L & p2f3) << (48 + 8 - 2)) | ((0x8L & p2f3) << (48 + 12 - 3));



				p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

				killerBoardsP1[i * 20 + 10] = ((0x1L & p1f0) << (0 + 1)) | ((0x2L & p1f0) << (4 - 1 + 1)) | ((0x4L & p1f0) << (8 - 2 + 1)) | ((0x8L & p1f0) << (12 - 3 + 1))
						| ((0x1L & p1f1) << (16 + 0 + 1)) | ((0x2L & p1f1) << (16 + 4 - 1 + 1)) | ((0x4L & p1f1) << (16 + 8 - 2 + 1)) | ((0x8L & p1f1) << (16 + 12 - 3 + 1))
						| ((0x1L & p1f2) << (32 + 0 + 1)) | ((0x2L & p1f2) << (32 + 4 - 1 + 1)) | ((0x4L & p1f2) << (32 + 8 - 2 + 1)) | ((0x8L & p1f2) << (32 + 12 - 3 + 1))
						| ((0x1L & p1f3) << (48 + 0 + 1)) | ((0x2L & p1f3) << (48 + 4 - 1 + 1)) | ((0x4L & p1f3) << (48 + 8 - 2 + 1)) | ((0x8L & p1f3) << (48 + 12 - 3 + 1));
				killerBoardsP2[i * 20 + 10] = ((0x1L & p2f0) << (0 + 1)) | ((0x2L & p2f0) << (4 - 1 + 1)) | ((0x4L & p2f0) << (8 - 2 + 1)) | ((0x8L & p2f0) << (12 - 3 + 1))
						| ((0x1L & p2f1) << (16 + 0 + 1)) | ((0x2L & p2f1) << (16 + 4 - 1 + 1)) | ((0x4L & p2f1) << (16 + 8 - 2 + 1)) | ((0x8L & p2f1) << (16 + 12 - 3 + 1))
						| ((0x1L & p2f2) << (32 + 0 + 1)) | ((0x2L & p2f2) << (32 + 4 - 1 + 1)) | ((0x4L & p2f2) << (32 + 8 - 2 + 1)) | ((0x8L & p2f2) << (32 + 12 - 3 + 1))
						| ((0x1L & p2f3) << (48 + 0 + 1)) | ((0x2L & p2f3) << (48 + 4 - 1 + 1)) | ((0x4L & p2f3) << (48 + 8 - 2 + 1)) | ((0x8L & p2f3) << (48 + 12 - 3 + 1));

				p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

				killerBoardsP1[i * 20 + 11] = ((0x1L & p1f0) << (0 + 1)) | ((0x2L & p1f0) << (4 - 1 + 1)) | ((0x4L & p1f0) << (8 - 2 + 1)) | ((0x8L & p1f0) << (12 - 3 + 1))
						| ((0x1L & p1f1) << (16 + 0 + 1)) | ((0x2L & p1f1) << (16 + 4 - 1 + 1)) | ((0x4L & p1f1) << (16 + 8 - 2 + 1)) | ((0x8L & p1f1) << (16 + 12 - 3 + 1))
						| ((0x1L & p1f2) << (32 + 0 + 1)) | ((0x2L & p1f2) << (32 + 4 - 1 + 1)) | ((0x4L & p1f2) << (32 + 8 - 2 + 1)) | ((0x8L & p1f2) << (32 + 12 - 3 + 1))
						| ((0x1L & p1f3) << (48 + 0 + 1)) | ((0x2L & p1f3) << (48 + 4 - 1 + 1)) | ((0x4L & p1f3) << (48 + 8 - 2 + 1)) | ((0x8L & p1f3) << (48 + 12 - 3 + 1));
				killerBoardsP2[i * 20 + 11] = ((0x1L & p2f0) << (0 + 1)) | ((0x2L & p2f0) << (4 + 1)) | ((0x4L & p2f0) << (8 - 2 + 1)) | ((0x8L & p2f0) << (12 - 3 + 1))
						| ((0x1L & p2f1) << (16 + 0 + 1)) | ((0x2L & p2f1) << (16 + 4 - 1 + 1)) | ((0x4L & p2f1) << (16 + 8 - 2 + 1)) | ((0x8L & p2f1) << (16 + 12 - 3 + 1))
						| ((0x1L & p2f2) << (32 + 0 + 1)) | ((0x2L & p2f2) << (32 + 4 - 1 + 1)) | ((0x4L & p2f2) << (32 + 8 - 2 + 1)) | ((0x8L & p2f2) << (32 + 12 - 3 + 1))
						| ((0x1L & p2f3) << (48 + 0 + 1)) | ((0x2L & p2f3) << (48 + 4 - 1 + 1)) | ((0x4L & p2f3) << (48 + 8 - 2 + 1)) | ((0x8L & p2f3) << (48 + 12 - 3 + 1));


				p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

				killerBoardsP1[i * 20 + 12] = ((0x1L & p1f0) << (0 + 2)) | ((0x2L & p1f0) << (4 - 1 + 2)) | ((0x4L & p1f0) << (8 - 2 + 2)) | ((0x8L & p1f0) << (12 - 3 + 2))
						| ((0x1L & p1f1) << (16 + 0 + 2)) | ((0x2L & p1f1) << (16 + 4 - 1 + 2)) | ((0x4L & p1f1) << (16 + 8 - 2 + 2)) | ((0x8L & p1f1) << (16 + 12 - 3 + 2))
						| ((0x1L & p1f2) << (32 + 0 + 2)) | ((0x2L & p1f2) << (32 + 4 - 1 + 2)) | ((0x4L & p1f2) << (32 + 8 - 2 + 2)) | ((0x8L & p1f2) << (32 + 12 - 3 + 2))
						| ((0x1L & p1f3) << (48 + 0 + 2)) | ((0x2L & p1f3) << (48 + 4 - 1 + 2)) | ((0x4L & p1f3) << (48 + 8 - 2 + 2)) | ((0x8L & p1f3) << (48 + 12 - 3 + 2));
				killerBoardsP2[i * 20 + 12] = ((0x1L & p2f0) << (0 + 2)) | ((0x2L & p2f0) << (4 - 1 + 2)) | ((0x4L & p2f0) << (8 - 2 + 2)) | ((0x8L & p2f0) << (12 - 3 + 2))
						| ((0x1L & p2f1) << (16 + 0 + 2)) | ((0x2L & p2f1) << (16 + 4 - 1 + 2)) | ((0x4L & p2f1) << (16 + 8 - 2 + 2)) | ((0x8L & p2f1) << (16 + 12 - 3 + 2))
						| ((0x1L & p2f2) << (32 + 0 + 2)) | ((0x2L & p2f2) << (32 + 4 - 1 + 2)) | ((0x4L & p2f2) << (32 + 8 - 2 + 2)) | ((0x8L & p2f2) << (32 + 12 - 3 + 2))
						| ((0x1L & p2f3) << (48 + 0 + 2)) | ((0x2L & p2f3) << (48 + 4 - 1 + 2)) | ((0x4L & p2f3) << (48 + 8 - 2 + 2)) | ((0x8L & p2f3) << (48 + 12 - 3 + 2));

				p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

				killerBoardsP1[i * 20 + 13] = ((0x1L & p1f0) << (0 + 2)) | ((0x2L & p1f0) << (4 - 1 + 2)) | ((0x4L & p1f0) << (8 - 2 + 2)) | ((0x8L & p1f0) << (12 - 3 + 2))
						| ((0x1L & p1f1) << (16 + 0 + 2)) | ((0x2L & p1f1) << (16 + 4 - 1 + 2)) | ((0x4L & p1f1) << (16 + 8 - 2 + 2)) | ((0x8L & p1f1) << (16 + 12 - 3 + 2))
						| ((0x1L & p1f2) << (32 + 0 + 2)) | ((0x2L & p1f2) << (32 + 4 - 1 + 2)) | ((0x4L & p1f2) << (32 + 8 - 2 + 2)) | ((0x8L & p1f2) << (32 + 12 - 3 + 2))
						| ((0x1L & p1f3) << (48 + 0 + 2)) | ((0x2L & p1f3) << (48 + 4 - 1 + 2)) | ((0x4L & p1f3) << (48 + 8 - 2 + 2)) | ((0x8L & p1f3) << (48 + 12 - 3 + 2));
				killerBoardsP2[i * 20 + 13] = ((0x1L & p2f0) << (0 + 2)) | ((0x2L & p2f0) << (4 - 1 + 2)) | ((0x4L & p2f0) << (8 - 2 + 2)) | ((0x8L & p2f0) << (12 - 3 + 2))
						| ((0x1L & p2f1) << (16 + 0 + 2)) | ((0x2L & p2f1) << (16 + 4 - 1 + 2)) | ((0x4L & p2f1) << (16 + 8 - 2 + 2)) | ((0x8L & p2f1) << (16 + 12 - 3 + 2))
						| ((0x1L & p2f2) << (32 + 0 + 2)) | ((0x2L & p2f2) << (32 + 4 - 1 + 2)) | ((0x4L & p2f2) << (32 + 8 - 2 + 2)) | ((0x8L & p2f2) << (32 + 12 - 3 + 2))
						| ((0x1L & p2f3) << (48 + 0 + 2)) | ((0x2L & p2f3) << (48 + 4 - 1 + 2)) | ((0x4L & p2f3) << (48 + 8 - 2 + 2)) | ((0x8L & p2f3) << (48 + 12 - 3 + 2));



				p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

				killerBoardsP1[i * 20 + 14] = ((0x1L & p1f0) << (0 + 3)) | ((0x2L & p1f0) << (4 - 1 + 3)) | ((0x4L & p1f0) << (8 - 2 + 3)) | ((0x8L & p1f0) << (12 - 3 + 3))
						| ((0x1L & p1f1) << (16 + 0 + 3)) | ((0x2L & p1f1) << (16 + 4 - 1 + 3)) | ((0x4L & p1f1) << (16 + 8 - 2 + 3)) | ((0x8L & p1f1) << (16 + 12 - 3 + 3))
						| ((0x1L & p1f2) << (32 + 0 + 3)) | ((0x2L & p1f2) << (32 + 4 - 1 + 3)) | ((0x4L & p1f2) << (32 + 8 - 2 + 3)) | ((0x8L & p1f2) << (32 + 12 - 3 + 3))
						| ((0x1L & p1f3) << (48 + 0 + 3)) | ((0x2L & p1f3) << (48 + 4 - 1 + 3)) | ((0x4L & p1f3) << (48 + 8 - 2 + 3)) | ((0x8L & p1f3) << (48 + 12 - 3 + 3));
				killerBoardsP2[i * 20 + 14] = ((0x1L & p2f0) << (0 + 3)) | ((0x2L & p2f0) << (4 - 1 + 3)) | ((0x4L & p2f0) << (8 - 2 + 3)) | ((0x8L & p2f0) << (12 - 3 + 3))
						| ((0x1L & p2f1) << (16 + 0 + 3)) | ((0x2L & p2f1) << (16 + 4 - 1 + 3)) | ((0x4L & p2f1) << (16 + 8 - 2 + 3)) | ((0x8L & p2f1) << (16 + 12 - 3 + 3))
						| ((0x1L & p2f2) << (32 + 0 + 3)) | ((0x2L & p2f2) << (32 + 4 - 1 + 3)) | ((0x4L & p2f2) << (32 + 8 - 2 + 3)) | ((0x8L & p2f2) << (32 + 12 - 3 + 3))
						| ((0x1L & p2f3) << (48 + 0 + 3)) | ((0x2L & p2f3) << (48 + 4 - 1 + 3)) | ((0x4L & p2f3) << (48 + 8 - 2 + 3)) | ((0x8L & p2f3) << (48 + 12 - 3 + 3));

				p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

				killerBoardsP1[i * 20 + 15] = ((0x1L & p1f0) << (0 + 3)) | ((0x2L & p1f0) << (4 - 1 + 3)) | ((0x4L & p1f0) << (8 - 2 + 3)) | ((0x8L & p1f0) << (12 - 3 + 3))
						| ((0x1L & p1f1) << (16 + 0 + 3)) | ((0x2L & p1f1) << (16 + 4 - 1 + 3)) | ((0x4L & p1f1) << (16 + 8 - 2 + 3)) | ((0x8L & p1f1) << (16 + 12 - 3 + 3))
						| ((0x1L & p1f2) << (32 + 0 + 3)) | ((0x2L & p1f2) << (32 + 4 - 1 + 3)) | ((0x4L & p1f2) << (32 + 8 - 2 + 3)) | ((0x8L & p1f2) << (32 + 12 - 3 + 3))
						| ((0x1L & p1f3) << (48 + 0 + 3)) | ((0x2L & p1f3) << (48 + 4 - 1 + 3)) | ((0x4L & p1f3) << (48 + 8 - 2 + 3)) | ((0x8L & p1f3) << (48 + 12 - 3 + 3));
				killerBoardsP2[i * 20 + 15] = ((0x1L & p2f0) << (0 + 3)) | ((0x2L & p2f0) << (4 - 1 + 3)) | ((0x4L & p2f0) << (8 - 2 + 3)) | ((0x8L & p2f0) << (12 - 3 + 3))
						| ((0x1L & p2f1) << (16 + 0 + 3)) | ((0x2L & p2f1) << (16 + 4 - 1 + 3)) | ((0x4L & p2f1) << (16 + 8 - 2 + 3)) | ((0x8L & p2f1) << (16 + 12 - 3 + 3))
						| ((0x1L & p2f2) << (32 + 0 + 3)) | ((0x2L & p2f2) << (32 + 4 - 1 + 3)) | ((0x4L & p2f2) << (32 + 8 - 2 + 3)) | ((0x8L & p2f2) << (32 + 12 - 3 + 3))
						| ((0x1L & p2f3) << (48 + 0 + 3)) | ((0x2L & p2f3) << (48 + 4 - 1 + 3)) | ((0x4L & p2f3) << (48 + 8 - 2 + 3)) | ((0x8L & p2f3) << (48 + 12 - 3 + 3));


				p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

				killerBoardsP1[i * 20 + 16] = ((0x1L & p1f0) << (0 + 0)) | ((0x2L & p1f0) << (4 - 1 + 1)) | ((0x4L & p1f0) << (8 - 2 + 2)) | ((0x8L & p1f0) << (12 - 3 + 3))
						| ((0x1L & p1f1) << (16 + 0 + 0)) | ((0x2L & p1f1) << (16 + 4 - 1 + 1)) | ((0x4L & p1f1) << (16 + 8 - 2 + 2)) | ((0x8L & p1f1) << (16 + 12 - 3 + 3))
						| ((0x1L & p1f2) << (32 + 0 + 0)) | ((0x2L & p1f2) << (32 + 4 - 1 + 1)) | ((0x4L & p1f2) << (32 + 8 - 2 + 2)) | ((0x8L & p1f2) << (32 + 12 - 3 + 3))
						| ((0x1L & p1f3) << (48 + 0 + 0)) | ((0x2L & p1f3) << (48 + 4 - 1 + 1)) | ((0x4L & p1f3) << (48 + 8 - 2 + 2)) | ((0x8L & p1f3) << (48 + 12 - 3 + 3));
				killerBoardsP2[i * 20 + 16] = ((0x1L & p2f0) << (0 + 0)) | ((0x2L & p2f0) << (4 - 1 + 1)) | ((0x4L & p2f0) << (8 - 2 + 2)) | ((0x8L & p2f0) << (12 - 3 + 3))
						| ((0x1L & p2f1) << (16 + 0 + 0)) | ((0x2L & p2f1) << (16 + 4 - 1 + 1)) | ((0x4L & p2f1) << (16 + 8 - 2 + 2)) | ((0x8L & p2f1) << (16 + 12 - 3 + 3))
						| ((0x1L & p2f2) << (32 + 0 + 0)) | ((0x2L & p2f2) << (32 + 4 - 1 + 1)) | ((0x4L & p2f2) << (32 + 8 - 2 + 2)) | ((0x8L & p2f2) << (32 + 12 - 3 + 3))
						| ((0x1L & p2f3) << (48 + 0 + 0)) | ((0x2L & p2f3) << (48 + 4 - 1 + 1)) | ((0x4L & p2f3) << (48 + 8 - 2 + 2)) | ((0x8L & p2f3) << (48 + 12 - 3 + 3));

				p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

				killerBoardsP1[i * 20 + 17] = ((0x1L & p1f0) << (0 + 0)) | ((0x2L & p1f0) << (4 - 1 + 1)) | ((0x4L & p1f0) << (8 - 2 + 2)) | ((0x8L & p1f0) << (12 - 3 + 3))
						| ((0x1L & p1f1) << (16 + 0 + 0)) | ((0x2L & p1f1) << (16 + 4 - 1 + 1)) | ((0x4L & p1f1) << (16 + 8 - 2 + 2)) | ((0x8L & p1f1) << (16 + 12 - 3 + 3))
						| ((0x1L & p1f2) << (32 + 0 + 0)) | ((0x2L & p1f2) << (32 + 4 - 1 + 1)) | ((0x4L & p1f2) << (32 + 8 - 2 + 2)) | ((0x8L & p1f2) << (32 + 12 - 3 + 3))
						| ((0x1L & p1f3) << (48 + 0 + 0)) | ((0x2L & p1f3) << (48 + 4 - 1 + 1)) | ((0x4L & p1f3) << (48 + 8 - 2 + 2)) | ((0x8L & p1f3) << (48 + 12 - 3 + 3));
				killerBoardsP2[i * 20 + 17] = ((0x1L & p2f0) << (0 + 0)) | ((0x2L & p2f0) << (4 - 1 + 1)) | ((0x4L & p2f0) << (8 - 2 + 2)) | ((0x8L & p2f0) << (12 - 3 + 3))
						| ((0x1L & p2f1) << (16 + 0 + 0)) | ((0x2L & p2f1) << (16 + 4 - 1 + 1)) | ((0x4L & p2f1) << (16 + 8 - 2 + 2)) | ((0x8L & p2f1) << (16 + 12 - 3 + 3))
						| ((0x1L & p2f2) << (32 + 0 + 0)) | ((0x2L & p2f2) << (32 + 4 - 1 + 1)) | ((0x4L & p2f2) << (32 + 8 - 2 + 2)) | ((0x8L & p2f2) << (32 + 12 - 3 + 3))
						| ((0x1L & p2f3) << (48 + 0 + 0)) | ((0x2L & p2f3) << (48 + 4 - 1 + 1)) | ((0x4L & p2f3) << (48 + 8 - 2 + 2)) | ((0x8L & p2f3) << (48 + 12 - 3 + 3));


				p1f0 = 0xFL & killerBoardsP1[i * 20 + 0];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 0]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 0]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 0]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 0];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 0]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 0]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 0]) >> 48;

				killerBoardsP1[i * 20 + 18] = ((0x1L & p1f0) << (0 + 3)) | ((0x2L & p1f0) << (4 - 1 + 2)) | ((0x4L & p1f0) << (8 - 2 + 1)) | ((0x8L & p1f0) << (12 - 3 + 0))
						| ((0x1L & p1f1) << (16 + 0 + 3)) | ((0x2L & p1f1) << (16 + 4 - 1 + 2)) | ((0x4L & p1f1) << (16 + 8 - 2 + 1)) | ((0x8L & p1f1) << (16 + 12 - 3 + 0))
						| ((0x1L & p1f2) << (32 + 0 + 3)) | ((0x2L & p1f2) << (32 + 4 - 1 + 2)) | ((0x4L & p1f2) << (32 + 8 - 2 + 1)) | ((0x8L & p1f2) << (32 + 12 - 3 + 0))
						| ((0x1L & p1f3) << (48 + 0 + 3)) | ((0x2L & p1f3) << (48 + 4 - 1 + 2)) | ((0x4L & p1f3) << (48 + 8 - 2 + 1)) | ((0x8L & p1f3) << (48 + 12 - 3 + 0));
				killerBoardsP2[i * 20 + 18] = ((0x1L & p2f0) << (0 + 3)) | ((0x2L & p2f0) << (4 - 1 + 2)) | ((0x4L & p2f0) << (8 - 2 + 1)) | ((0x8L & p2f0) << (12 - 3 + 0))
						| ((0x1L & p2f1) << (16 + 0 + 3)) | ((0x2L & p2f1) << (16 + 4 - 1 + 2)) | ((0x4L & p2f1) << (16 + 8 - 2 + 1)) | ((0x8L & p2f1) << (16 + 12 - 3 + 0))
						| ((0x1L & p2f2) << (32 + 0 + 3)) | ((0x2L & p2f2) << (32 + 4 - 1 + 2)) | ((0x4L & p2f2) << (32 + 8 - 2 + 1)) | ((0x8L & p2f2) << (32 + 12 - 3 + 0))
						| ((0x1L & p2f3) << (48 + 0 + 3)) | ((0x2L & p2f3) << (48 + 4 - 1 + 2)) | ((0x4L & p2f3) << (48 + 8 - 2 + 1)) | ((0x8L & p2f3) << (48 + 12 - 3 + 0));

				p1f0 = 0xFL & killerBoardsP1[i * 20 + 1];
				p1f1 = (0xF0000L & killerBoardsP1[i * 20 + 1]) >> 16;
				p1f2 = (0xF00000000L & killerBoardsP1[i * 20 + 1]) >> 32;
				p1f3 = (0xF000000000000L & killerBoardsP1[i * 20 + 1]) >> 48;
				p2f0 = 0xFL & killerBoardsP2[i * 20 + 1];
				p2f1 = (0xF0000L & killerBoardsP2[i * 20 + 1]) >> 16;
				p2f2 = (0xF00000000L & killerBoardsP2[i * 20 + 1]) >> 32;
				p2f3 = (0xF000000000000L & killerBoardsP2[i * 20 + 1]) >> 48;

				killerBoardsP1[i * 20 + 19] = ((0x1L & p1f0) << (0 + 3)) | ((0x2L & p1f0) << (4 - 1 + 2)) | ((0x4L & p1f0) << (8 - 2 + 1)) | ((0x8L & p1f0) << (12 - 3 + 0))
						| ((0x1L & p1f1) << (16 + 0 + 3)) | ((0x2L & p1f1) << (16 + 4 - 1 + 2)) | ((0x4L & p1f1) << (16 + 8 - 2 + 1)) | ((0x8L & p1f1) << (16 + 12 - 3 + 0))
						| ((0x1L & p1f2) << (32 + 0 + 3)) | ((0x2L & p1f2) << (32 + 4 - 1 + 2)) | ((0x4L & p1f2) << (32 + 8 - 2 + 1)) | ((0x8L & p1f2) << (32 + 12 - 3 + 0))
						| ((0x1L & p1f3) << (48 + 0 + 3)) | ((0x2L & p1f3) << (48 + 4 - 1 + 2)) | ((0x4L & p1f3) << (48 + 8 - 2 + 1)) | ((0x8L & p1f3) << (48 + 12 - 3 + 0));
				killerBoardsP2[i * 20 + 19] = ((0x1L & p2f0) << (0 + 3)) | ((0x2L & p2f0) << (4 - 1 + 2)) | ((0x4L & p2f0) << (8 - 2 + 1)) | ((0x8L & p2f0) << (12 - 3 + 0))
						| ((0x1L & p2f1) << (16 + 0 + 3)) | ((0x2L & p2f1) << (16 + 4 - 1 + 2)) | ((0x4L & p2f1) << (16 + 8 - 2 + 1)) | ((0x8L & p2f1) << (16 + 12 - 3 + 0))
						| ((0x1L & p2f2) << (32 + 0 + 3)) | ((0x2L & p2f2) << (32 + 4 - 1 + 2)) | ((0x4L & p2f2) << (32 + 8 - 2 + 1)) | ((0x8L & p2f2) << (32 + 12 - 3 + 0))
						| ((0x1L & p2f3) << (48 + 0 + 3)) | ((0x2L & p2f3) << (48 + 4 - 1 + 2)) | ((0x4L & p2f3) << (48 + 8 - 2 + 1)) | ((0x8L & p2f3) << (48 + 12 - 3 + 0));

			}
		}
		
		private static long mirrorSingle(long single)
		{
			if (single == 0x0L)
				return 0x0L;
			else if (single == 0x1L)
				return 0x8L;
			else if (single == 0x2L)
				return 0x4L;
			else if (single == 0x3L)
				return 0xCL;
			else if (single == 0x4L)
				return 0x2L;
			else if (single == 0x5L)
				return 0xAL;
			else if (single == 0x6L)
				return 0x6L;
			else if (single == 0x7L)
				return 0xEL;
			else if (single == 0x8L)
				return 0x1L;
			else if (single == 0x9L)
				return 0x9L;
			else if (single == 0xAL)
				return 0x5L;
			else if (single == 0xBL)
				return 0xDL;
			else if (single == 0xCL)
				return 0x3L;
			else if (single == 0xDL)
				return 0xBL;
			else if (single == 0xEL)
				return 0x7L;
			else if (single == 0xFL)
				return 0xFL;
			else
			{
				System.out.println("One of the above cases should fit :(");
				System.exit(-1);
				return 0x0L;
			}
		}
	
	
	public static boolean hasGameEnded(long bp)
	{
		for (int i = 0; i < longLines.length; i++)
		{
			if ((bp & longLines[i]) == longLines[i])
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean killerMove(long bp1, long bp2)
	{
		if (killerBoardsP1.length == 0)
		{
			fillKillerBoards();
		}
		
		for (int i = 0; i < killerBoardsP1.length; i++)
		{
			long self = bp1 & killerBoardsP1[i];
			long other = bp2 & killerBoardsP2[i];
			
			if (self == killerBoardsP1[i] && other == 0x0L)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static long getBP1FromGame(SogoGame g)
	{
		long bp = 0x0L;
		for (int z = 3; z >= 0; z--)
		{
			for (int y = 3; y >= 0; y--)
			{
				for (int x = 3; x >= 0; x--)
				{
					bp = (bp << 1) | (g.board[x][y][z] == g.getCurrentPlayer() ? 0x1L : 0x0L);
				}
			}
		}
		
		return bp;
	}
	
	public static long getBP2FromGame(SogoGame g)
	{
		long bp = 0x0L;
		for (int z = 3; z >= 0; z--)
		{
			for (int y = 3; y >= 0; y--)
			{
				for (int x = 3; x >= 0; x--)
				{
					bp = (bp << 1) | (g.board[x][y][z] == g.getOtherPlayer() ? 0x1L : 0x0L);
				}
			}
		}
		
		return bp;
	}
	
}
