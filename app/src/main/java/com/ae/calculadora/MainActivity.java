package com.ae.calculadora;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button nOne;
    String aux="",presButton="";
    String expr = "";
    
    Boolean signo = false;

    public boolean isOperator(String op){
        return op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/");
    }

    public  boolean isTrig(String op){
        return op.equals("Sin") || op.equals("Cos") || op.equals("Tan") || op.equals("Ln");
    }
    public int pre(String op)
    {
        if(op.equals("Sin") || op.equals("Cos") || op.equals("Tan") || op.equals("Ln"))
            return 3;
        if (op.equals("*") || op.equals("/"))
            return 2;
        if (op.equals("+") || op.equals("-"))
            return 1;
        return 0;
    }

    public String evalua(String in){

        String [] tokens = in.split("@");
        System.out.println(tokens.length);
        String pos = postfix(tokens);
        String [] ptokens = pos.split("@");
        float n1,n2,res;
        Stack<String> ex = new Stack<String>();
        for(String token : ptokens){
            if(isOperator(token)){
                n2 = Float.parseFloat(ex.pop());
                n1 = Float.parseFloat(ex.pop());
                switch (token){
                    case "+":
                        res = n1+n2;
                        ex.push(""+res);
                        break;
                    case "-":
                        res = n1-n2;
                        ex.push(""+res);
                        break;
                    case "*":
                        res = n1*n2;
                        ex.push(""+res);
                        break;
                    case "/":
                        res = n1/n2;
                        ex.push(""+res);
                        break;
                }
            }else if(isTrig(token)){
                System.out.println("Token trig:" + token);
                n1 = Float.parseFloat(ex.pop());
                switch(token){
                    case "Sin":
                        res = (float) Math.sin(n1);
                        ex.push(""+res);
                        break;
                    case "Cos":
                        res = (float) Math.cos(n1);
                        ex.push(""+res);
                        break;
                    case "Tan":
                        res = (float) Math.tan(n1);
                        ex.push(""+res);
                        break;
                    case "Ln":
                        res = (float) Math.log(n1);
                        ex.push(""+res);
                        break;
                }
            }else{
                ex.push(token);
            }
        }
        return ex.peek();
    }

    public String postfix(String [] tokens){
        String pos ="";
        Stack<String> sss = new Stack<String>();
        for(String token : tokens)
        {
            if(token.equals("(")){
                sss.push(token);
            }else if(token.equals(")")){
                while(!sss.peek().equals("(")){
                    pos+=sss.pop()+"@";
                }
                sss.pop();
            } else if(isOperator(token) || isTrig(token)){
                if(sss.empty()){
                    sss.push(token);
                }else{
                    if(pre(token) == pre(sss.peek())){
                        pos+=sss.pop() + "@";
                        sss.push(token);
                    }else if(pre(token) > pre(sss.peek())){
                        sss.push(token);
                    }else if(pre(token) < pre(sss.peek())){
                        while(!sss.empty() && !sss.peek().equals("(")){
                            pos+=sss.pop()+"@";
                        }
                        sss.push(token);
                    }
                }
            }else{
                pos+=token+"@";
            }
            System.out.println("Token"+token);
            System.out.println(sss.toString());
        }

        while(!sss.empty()){
            pos+=sss.pop()+"@";
        }
        System.out.println(pos);
        return pos;
    }

    public boolean balance(String exp){
        if(exp.length()==0) return true;
        char[] expA = exp.toCharArray();
        Stack<Character> cStack = new Stack<Character>();
        for (char C: expA) {
            if(C == '('){
                cStack.push(C);
            }
            if(C == ')')
                if(cStack.empty()) return false;
                else{
                cStack.pop();
                }
        }
        if(cStack.empty()) return true;
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         nOne = findViewById(R.id.nOne);
    }

    @Override
    public void onClick(View v) {
        presButton = ((Button)v).getText().toString();
        if(isOperator(presButton)){
            if(aux.equals("")){
                if(presButton.equals("-")){
                        aux+=presButton;
                        expr=aux;
                        signo=true;
                }
            }else{
                if(!signo){
                    if(expr.equals(""))
                        expr += aux + "@" + presButton +"@";
                    else
                        expr+=  "@" + presButton + "@";
                    System.out.println("Expresion arrobas:"+expr);
                    aux+=presButton;
                    signo=true;
                }
            }

        }else if(isTrig(presButton)){
            if(expr.equals(""))
                expr += aux + presButton +"@(@";
            else
                expr+=  "@" + presButton + "@(@";
            aux+=presButton+"(";
        }
        else if(presButton.equals("C")) {
            aux = "";
            expr="";
        }
        else if(presButton.equals("=")){
            if(balance(aux) && !aux.equals("")){
                System.out.println(expr);
                aux= evalua(expr);
            }else{
                aux="sin balance";
            }
        }
        else if(presButton.equals("DEL")){
            if(aux.length()>0) {
                if (expr.lastIndexOf("@") != -1) {
                    aux = expr.substring(0, expr.lastIndexOf("@"));
                    aux=aux.replace("@","");
                    expr= expr.substring(0, expr.lastIndexOf("@"));
                }
                else {
                    if(aux.startsWith("Sin")||aux.startsWith("Cos")||aux.startsWith("Tan")||aux.startsWith("Ln")){
                        aux="";
                        expr="";
                    }else {
                        aux = aux.substring(0, aux.length() - 1);
                        expr = expr.substring(0, expr.length() - 1);
                    }
                }
            }
        }else if(presButton.equals(")")){
            expr+="@)";
            aux+=")";
        }
        else {
            signo=false;
            expr += ((Button) v).getText();
            aux += ((Button) v).getText();

        }
        ((TextView) (findViewById(R.id.CalcText))).setText(aux);
    }
}