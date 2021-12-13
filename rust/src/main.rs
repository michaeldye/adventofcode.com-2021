/*
read and parse a file of integers
 */

mod aoc;

use std::env;

fn help() {
    println!("usage:
aocrust <day_number>
    Execute a certain day");
}

fn main() {
    let args: Vec<String> = env::args().collect();

    match args.len() {
        // one argument passed
        2 => {
            match args[1].parse() {
                Ok(1) => aoc::day1::day1(),
                Ok(2) => aoc::day2::day2(),
                _ => println!("Invalid day!"),
            }
        },
        // show a help message
        _ => {
            help();
        }
    }
}