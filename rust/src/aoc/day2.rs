use std::fs::File;
use std::io::Read;
use std::str::FromStr;

#[derive(PartialEq)]
enum Direction {
    Forward,
    Down,
    Up,
}

impl FromStr for Direction {
    type Err = ();

    fn from_str(s: &str) -> Result<Direction, Self::Err> {
        use Direction::*;
        match s {
            "forward" => Ok(Forward),
            "down" => Ok(Down),
            "up" => Ok(Up),
            _ => Err(())
        }
    }
}

struct Instruction(Direction, i32);

pub fn day2() {
    for filename in ["input/day2_example1.txt", "input/day2.txt"] {
        part_one(filename);
    }
    for filename in ["input/day2_example1.txt", "input/day2.txt"] {
        part_two(filename);
    }
}

fn part_two(filename: &str) {
    let dirs = load_instructions(filename);

    let mut depth = 0;
    let mut horizontal = 0;
    let mut aim = 0;

    for dir in dirs {
        match dir {
            Instruction(Direction::Forward, m) => {
                horizontal += m;
                depth += aim * m;
            },
            Instruction(Direction::Up, m) => {
                aim -= m;
            },
            Instruction(Direction::Down, m) => {
                aim += m;
            },
        }
    }

    println!("h: {} d: {} a: {}", horizontal, depth, aim);
    println!("{}", horizontal * depth);
}

fn part_one(filename: &str) {
    let dirs = load_instructions(filename);

    let mut depth = 0;
    let mut horizontal = 0;

    for dir in dirs {
        match dir {
            Instruction(Direction::Forward, m) => horizontal += m,
            Instruction(Direction::Up, m) => depth -= m,
            Instruction(Direction::Down, m) => depth += m,
        }
    }

    println!("h: {} d: {}", horizontal, depth);
    println!("{}", horizontal * depth);
}

fn parse_line(s: &str) -> Instruction {
    match s.split_whitespace().collect::<Vec<&str>>().as_slice() {
        [d, m] => Instruction(Direction::from_str(d).unwrap(), m.parse().unwrap()),
        _ => panic!("could not parse line!")
    }
}

fn load_instructions(filename: &str) -> Vec<Instruction> {
    let mut buf = String::new();
    File::open(filename)
        .expect("file not opened!")
        .read_to_string(&mut buf)
        .expect("could not read file!");
    buf
        .lines()
        .map(|x| parse_line(x))
        .collect::<Vec<Instruction>>()
}