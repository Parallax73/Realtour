import { definePreset } from '@primeng/themes';
import Aura from '@primeng/themes/aura';

const BlueTheme = definePreset(Aura, {
    semantic: {
        primary: {
            50: '{blue.50}',
            100: '{blue.100}',
            200: '{blue.200}',
            300: '{blue.300}',
            400: '{blue.400}',
            500: '{blue.500}',
            600: '{blue.600}',
            700: '{blue.700}',
            800: '{blue.800}',
            900: '{blue.900}',
            950: '{blue.950}'
        },
          surface: {
            50: '{slate.50}',
            100: '{slate.100}',
            200: '{slate.200}',
            300: '{slate.300}',
            400: '{slate.400}',
            500: '{slate.500}',
            600: '{slate.600}',
            700: '{slate.700}',
            800: '{slate.800}',
            900: '{slate.900}',
            950: '{slate.950}'
        },





        colorScheme: {
            light: {
                primary: {
                    color: '{blue.700}',          
                    contrastColor: '{blue.50}',  
                    hoverColor: '{blue.800}',     
                    activeColor: '{blue.900}'     
                },
                surface: {
                    color: '{slate.100}',       
                    hover: '{slate.200}',
                    border: '{slate.300}'         
                },
                highlight: {
                    background: '{blue.50}',  
                    color: '{blue.700}',       
                    hoverColor: '{blue.100}',   
                    focusColor: '{blue.700}'     
                },
                root:{
                    background: '{slate.400}'
                }
            },
            dark: {
                primary: {
                    color: '{blue.400}',          
                    contrastColor: '{slate.900}', 
                    hoverColor: '{blue.300}',    
                    activeColor: '{blue.200}'     
                },
                surface: {
                    color: '{slate.700}',       
                    hover: '{slate.600}',       
                    border: '{slate.500}'         
                },
                highlight: {
                    background: '{blue.900}',     
                    color: '{blue.300}',         
                    hoverColor: '{blue.800}',    
                    focusColor: '{blue.400}'      
                }
                ,
                root:{
                    background: '{slate.700}'
                }
            }
        }
    }
});

export default {
    preset: BlueTheme,
    options: {
        darkModeSelector: '.my-app-dark'
    }
};
