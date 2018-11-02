package com.example.dll;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface AddDll extends Library{

	AddDll addDll = (AddDll) Native.loadLibrary("AddDll", AddDll.class);
	
	public int add(int a,int b); 
}
