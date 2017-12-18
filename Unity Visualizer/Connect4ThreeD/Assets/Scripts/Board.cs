using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using UnityEngine;
using UnityEngine.UI;

public class Ball
{
    public int x;
    public int y;
    public int z;
    public int color;

    public static Ball[] FromFile(string[] lines)
    {
        List<Ball> balls = new List<Ball>();

        for (int i = 0; i < lines.Length; i++)
        {
            string[] split = lines[i].Split(',');

            if (split.Length == 4)
            {
                try
                {
                    Ball newBall = new Ball();
                    newBall.color = Convert.ToInt32(split[0]);
                    newBall.x = Convert.ToInt32(split[1]);
                    newBall.z = Convert.ToInt32(split[2]);
                    newBall.y = Convert.ToInt32(split[3]);

                    balls.Add(newBall);
                }
                catch (Exception ex)
                {

                }
            }
        }

        return balls.ToArray();
    }
}

public class BoardConfig
{
    public int width = 1;
    public int height = 1;

    public BoardConfig(string firstLine)
    {
        string[] split = firstLine.Split(',');
        if (split.Length == 2)
        {
            try
            {
                width = Convert.ToInt32(split[0]);
                height = Convert.ToInt32(split[1]);
            }
            catch (Exception ex)
            {

            }
        }
    }

    public static bool operator ==(BoardConfig b1, BoardConfig b2)
    {
        return b1.width == b2.width && b1.height == b2.height;
    }

    public static bool operator !=(BoardConfig b1, BoardConfig b2)
    {
        return b1.width != b2.width || b1.height != b2.height;
    }
}

public class Board : MonoBehaviour
{
    [SerializeField]
    private string filePath = "";
    [SerializeField]
    private float gridWidth = 1f;

    [Space]
    
    [SerializeField]
    private GameObject pilePrefab;
    [SerializeField]
    private GameObject ballPrefab;
    [SerializeField]
    private InputField inputFilePath;
    [SerializeField]
    private Text textErrorFile;



    private List<GameObject> instPiles = new List<GameObject>();
    private List<GameObject> instBalls = new List<GameObject>();

    private List<Ball> balls = new List<Ball>();
    private BoardConfig config = new BoardConfig("1,1");

    [HideInInspector]
    public static float GridWidth = 1f;

	// Use this for initialization
	void Start ()
    {
        GridWidth = gridWidth;

        if (File.Exists(".\\latestFilePath.txt"))
        {
            inputFilePath.text = File.ReadAllText(".\\latestFilePath.txt");
            filePath = File.ReadAllText(".\\latestFilePath.txt");
        }
	}
	
	// Update is called once per frame
	void Update ()
    {
        if (gridWidth != GridWidth)
        {
            GridWidth = gridWidth;
            for (int i = 0; i < instBalls.Count; i++)
            {
                instBalls[i].GetComponent<InstBall>().Ball = balls[i];
            }
        }

		if (File.Exists(filePath))
        {
            string[] fileContent = new string[0];
            try
            {
                fileContent = File.ReadAllLines(filePath);
            }
            catch (Exception ex)
            {

            }

            if (fileContent.Length >= 1)
            {
                BoardConfig newConfig = new BoardConfig(fileContent[0]);
                if (newConfig != config)
                {
                    config = newConfig;
                    for (int i = 0; i < instPiles.Count; i++)
                    {
                        Destroy(instPiles[i]);
                    }
                    instPiles.Clear();

                    for (int x = 0; x < config.width; x++)
                    {
                        for (int z = 0; z < config.height; z++)
                        {
                            GameObject instPile = Instantiate(pilePrefab, transform);
                            instPile.transform.position = new Vector3(x * gridWidth, 0f, z * gridWidth);
                            instPiles.Add(instPile);
                        }
                    }
                }
            }
            
            if (fileContent.Length >= 2)
            {
                Ball[] newBalls = Ball.FromFile(fileContent);

                while (newBalls.Length > balls.Count)
                {
                    GameObject instBall = Instantiate(ballPrefab);
                    instBall.GetComponent<InstBall>().Ball = newBalls[balls.Count];
                    instBalls.Add(instBall);
                    balls.Add(newBalls[balls.Count]);
                }

                if (newBalls.Length < balls.Count)
                {
                    for (int i = 0; i < instBalls.Count; i++)
                    {
                        Destroy(instBalls[i]);
                    }
                    instBalls.Clear();
                    balls.Clear();
                }
            }
        }
	}

    public void InputFileChanged()
    {
        filePath = inputFilePath.text;

        if (!File.Exists(filePath))
        {
            textErrorFile.text = "File not found";
        }
        else
        {
            textErrorFile.text = "";
        }

        File.WriteAllText(".\\latestFilePath.txt", filePath);
    }
}