﻿using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class InstBall : MonoBehaviour {

    [SerializeField]
    private float lerpSpeed = 0.1f;
    [SerializeField]
    private float yStartOffest = 10f;
    [SerializeField]
    private Color[] colors;
    [SerializeField]
    private Material copyMaterial;

    public int color;

    private Ball finalPosition = new Ball();

	// Use this for initialization
	void Start ()
    {
		if (GameObject.Find("Canvas").transform.Find("InputField Dropspeed") != null)
        {
            string inputLerpSpeed = GameObject.Find("Canvas").transform.Find("InputField Dropspeed").GetComponent<InputField>().text;

            try
            {
                lerpSpeed = Convert.ToSingle(inputLerpSpeed);
            }
            catch (Exception ex)
            {

            }
        }
	}
	
	// Update is called once per frame
	void Update ()
    {
        transform.position = Vector3.Lerp(transform.position, new Vector3(finalPosition.x * Board.GridWidth, finalPosition.y * Board.GridWidth + Board.GridWidth * 0.5f, finalPosition.z * Board.GridWidth), lerpSpeed);
	}



    public Ball Ball
    {
        get
        {
            return finalPosition;
        }
        set
        {
            finalPosition = value;

            transform.position = new Vector3(finalPosition.x * Board.GridWidth, finalPosition.y * Board.GridWidth + yStartOffest + Board.GridWidth * 0.5f, finalPosition.z * Board.GridWidth);

            GetComponentInChildren<MeshRenderer>().materials[0] = new Material(GetComponentInChildren<MeshRenderer>().materials[0]);
            if (finalPosition.color >= 0 && finalPosition.color < colors.Length)
            {
                GetComponentInChildren<MeshRenderer>().materials[0].color = colors[finalPosition.color];
            }

            color = finalPosition.color;
        }
    }
}
