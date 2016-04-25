// Metodo per correggere la direzione dell'utente (in navigazione)
    public void adjustDirection(float degree, float direction){

        //TODO Aggiusta 'sto codice.
        if(direction-range < 0){ // Esempio: direction=10
            if( (degree >=0 && degree < (direction+range)) || (degree > direction-range && degree < 0) ){
                //TODO informa che sta nella giusta direzione
            }
            else if(degree >= direction+180-range && degree<=direction+180+range){
                //TODO informa che sta nella direzione opposta
            }
            float distanceClockwise        = direction+360-degree;
            float distanceCounterClockwise = degree-direction;
            if( distanceClockwise < distanceCounterClockwise ){
                //TODO informa che sta a sinistra
            }
            else{
                //TODO informa che sta a destra
            }
        }
        else if( (direction+range)%360 < direction-range ){ //Esempio: direction=350
            if( (degree >=0 && degree < (direction+range)%360) || (degree > direction-range && degree < 360) ){
                //TODO informa che sta nella giusta direzione
            }
            else if(degree >= direction-180-range && degree<=direction-180+range){
                //TODO informa che sta nella direzione opposta
            }
            float distanceClockwise        = direction-degree;
            float distanceCounterClockwise = degree-(direction-360);
            if( distanceClockwise < distanceCounterClockwise ){
                //TODO informa che sta a sinistra
            }
            else{
                //TODO informa che sta a destra
            }
        }

        if(direction >= 180.0f ){
            if( degree >= direction-180-range && degree <= direction-180+range ){
                //TODO informa che sta nella direzione opposta
            }
            else if(degree < direction-range && degree < direction+range){
                float distanceClockwise        = direction - degree;
                float distanceCounterClockwise = degree - (direction-360);
                if( distanceClockwise <= distanceCounterClockwise ){
                    //TODO informa che sta a sinistra
                }
                else{
                    //TODO informa che sta a destra
                }
            }
            else if(degree > direction+range){
                //TODO informa che sta a destra
            }
            else if(degree < direction-range){
                //TODO informa che sta a sinistra
            }
            else if(degree >= direction-range && degree <= direction+range){
                //TODO informa che sta nella direzione giusta
            }
            else{
                Log.d(TAG_DEBUG, "Durante adjustDirection, sono finito in un caso imprevisto:");
                Log.d(TAG_DEBUG, "degree="+degree+", direction="+direction);
            }
        }
        else{ // Caso in cui direction < 180.0
            if( degree >= direction+180-range && degree <= direction+180+range ){
                //TODO informa che sta nella direzione opposta
            }
            else if(degree > direction-range && degree > direction+range){
                float distanceClockwise        = direction+360 - degree;
                float distanceCounterClockwise = degree - direction;
                if( distanceClockwise <= distanceCounterClockwise ){
                    //TODO informa che sta a sinistra
                }
                else{
                    //TODO informa che sta a destra
                }
            }
            else if(degree > direction+range){
                //TODO informa che sta a destra
            }
            else if(degree < direction-range){
                //TODO informa che sta a sinistra
            }
            else if(degree >= direction-range && degree <= direction+range){
                //TODO informa che sta nella direzione giusta
            }
            else{
                Log.d(TAG_DEBUG, "Durante adjustDirection, sono finito in un caso imprevisto:");
                Log.d(TAG_DEBUG, "degree="+degree+", direction="+direction);
            }
        }
        //tv.append("\ndirection: "+direction+"\nmsg: "+msg);
    }