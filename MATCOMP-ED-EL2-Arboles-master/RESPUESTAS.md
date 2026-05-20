# RESPUESTAS PL2a - Árbol binario

3.
    iv. Verifica que la suma es la misma cuando se suman los elementos de los subárboles izquierdo y derecho. ¿Por qué?

        La suma es la misma cuando se suman los elementos de los subárboles izquierdo y derecho junto con la raíz.
        Esto ocurre porque un árbol binario se divide en tres partes: el subárbol izquierdo, la raíz y el subárbol derecho.
        Por eso, para obtener la suma total del árbol, hay que sumar esas tres partes.
        No sería suficiente sumar solo el subárbol izquierdo y el subárbol derecho, porque faltaría el valor almacenado en la raíz.
        Por tanto:
        suma total = suma subárbol izquierdo + raíz + suma subárbol derecho.
        El resultado coincide con getSuma() porque se están sumando todos los nodos del árbol exactamente una vez.

    v. ¿Cuál es la altura del árbol?

        8.

    vi. ¿Cuál es el camino para llegar al valor 110? ¿Cuál es su longitud de camino?

        El camino es [63, 95, 111, 103, 107, 109, 110] y la longitud de el camino es 6.

4.
    iv. Verifica que la suma es la misma cuando se suman los elementos de los subárboles izquierdo y derecho. ¿Por qué?

        En el árbol creado con números aleatorios, la suma también coincide cuando se suman los elementos del subárbol izquierdo,
        los elementos del subárbol derecho y el valor de la raíz. Esto ocurre porque, aunque los números se hayan insertado en orden aleatorio,
        el árbol sigue estando formado por tres partes principales:el subárbol izquierdo, la raíz y el subárbol derecho.
        Por tanto, la suma total del árbol se sigue calculando de la misma forma que antes, ya que los valores numericos siguen siendo todos los número de el 0 al 128.

    v. ¿Cuál es la altura del árbol? ¿por qué?

        La altura del árbol es 8 porque se insertan 129 números, desde el 0 hasta el 128, y el árbol se va equilibrando mediante rotaciones.
        En mi código, cada vez que se añade un dato con add(), se llama al método equilibrarArbol(). Este método comprueba si un lado del árbol pesa mucho más que el otro y,
        si hace falta, realiza giros a la izquierda o a la derecha. Gracias a eso, el árbol no queda inclinado como una lista, sino que se mantiene bastante equilibrado.
        Por eso normalmente la altura se mantiene en 8.

    vi. ¿Cuál es el camino para llegar al valor 110? ¿Cuál es su longitud de camino?

        En el árbol aleatorio, el camino para llegar al valor 110 depende del orden en el que se hayan insertado los números.
        Ya que el 110 no siempre va a estar en el mismo nivel respecto a la raiz ni en el mismo nodo, ya que se insertan numeros aleatoriamente.

* Explique las diferencias (si las ha habido) de los resultados obtenidos entre los dos programas de prueba.

    En los dos programas la suma total es la misma, porque se insertan los mismos números: del 0 al 128. Por eso getSuma() da 8256 en los dos casos.
    También da la misma suma usando preorden, orden central y postorden, porque los tres recorridos pasan por todos los nodos del árbol.
    Lo único que cambia entre ellos es el orden en el que recorren los datos. La diferencia principal está en la forma interna del árbol.
    En el primer programa los números se insertan en orden, mientras que en el segundo se insertan de forma aleatoria. Esto puede hacer que cambien la raíz, los subárboles y el camino hasta un número concreto, como el 110.
    En mi implementación, el árbol se equilibra mediante rotaciones, por eso la altura se mantiene baja. En el programa ordenado la altura es 8. En el programa aleatorio normalmente también sale 8, aunque podría variar un poco dependiendo del orden aleatorio de inserción.

* ¿Qué sucede con los resultados si ejecuta los programas de prueba varias veces?

    El programa ordenado siempre da los mismos resultados, porque los números se insertan siempre en el mismo orden.
    El programa aleatorio puede cambiar algunos resultados cada vez que se ejecuta, porque el orden de inserción cambia.
    La suma total seguirá siendo siempre 8256, pero pueden cambiar el camino hasta el valor 110, la longitud de ese camino, la raíz y la forma del árbol.
    La altura suele mantenerse en 8 porque el árbol está equilibrado, aunque en alguna ejecución podría cambiar ligeramente.

# RESPUESTAS PL2b - Grafos

## Camino mínimo entre dos entidades

Para calcular el camino mínimo usamos BFS.
BFS usa una cola y visita primero los nodos más cercanos al origen.
Además de la cola, guardamos los nodos visitados y también de qué nodo venimos. Esto nos permite reconstruir el camino al final.
Por ejemplo, si llegamos a D desde B, y a B desde A, podemos reconstruir el camino A -> B -> D. 
En el main vemos un ejemplo de camino minimo entre Einstein y Ulm, quedando esto: Camino minimo Einstein Ulm:
[persona:Einstein, lugar:Ulm]

## Grafo disjunto

Un grafo es disjunto si está separado en varias partes.
Para comprobarlo hacemos un BFS no dirigido desde el primer nodo.
Si el recorrido visita todos los nodos, el grafo no es disjunto; sino, sí que es disjunto.
Usamos BFS no dirigido porque si tenemos en cuenta el sentido de las flechas, habrá casos en los que parezca que es disjunto por no poder llegar a ciertos nodos, pero estos estén conectados
Para comprobarlo hemos creado dos archivos: conectado.json y disjunto.json, en el segundo hay grupos separados y en el primero no.

## Einstein

¿Qué físico famoso nació en la misma ciudad que Einstein?
Lo primero es buscar dónde nació Einstein, después recorremos las personas del grafo y comprobamos si son físicos, si tienen premio Nobel de Física y si nacieron en la misma ciudad que Einstein.
Con nuestro JSON la respuesta es persona:MaxBorn.

## Tripleta de Antonio y lugares de nacimiento de premios Nobel

Antonio se añade con la tripleta <"persona:Antonio", "nace_en", "lugar:Villarrubia de los Caballeros">
Para sacar los lugares de nacimiento de premios Nobel recorremos las personas,
para cada persona miramos si tiene una relación premio y si tiene una relación nace_en.
Si las dos se cumplen, añadimos su lugar de nacimiento a la lista.
Antonio no sale como premio Nobel porque no tiene relación premio.
Los caminos que se recorren son persona --premio--> premio y persona --nace_en--> lugar.


## Tipos de nodos

En el grafo los nodos son el sujeto (nodo de origen) y el objeto (nodo destino) que aparecen en las tripletas.
Los tipos que usamos son persona, lugar, premio, profesión y sin_tipo.
El tipo se obtiene mirando lo que aparece antes de los dos puntos. Por ejemplo, persona:Einstein es de tipo persona y 1921 es sin_tipo porque no tiene dos puntos.

## ¿Qué es una ontología?

Una ontología sirve para definir qué tipos de cosas hay en un problema y qué relaciones pueden tener entre ellas.
En nuestro caso, una persona puede nacer en un lugar, puede tener una profesión y puede tener un premio.

## Relación entre ontología y grafos

Un grafo sirve para representar una ontología porque los nodos son elementos y los arcos sus relaciones.
Por ejemplo, la tripleta persona:Einstein, nace_en, lugar:Ulm se guarda como un arco que va desde persona:Einstein hasta lugar:Ulm con la relación nace_en.

## Ontología en nuestro problema

Nuestra ontología tendría tipos como persona, lugar, premio y profesión.
También tendría relaciones como nace_en, premio y profesión, lo que ayuda a saber qué significa cada nodo y cada arco del grafo.

